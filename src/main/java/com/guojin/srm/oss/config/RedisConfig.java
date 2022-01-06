package com.guojin.srm.oss.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Configuration
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    private final static int SENTINEL=3;

    public final static String SRM_CACHE="SRM:CACHE";

    @Autowired
    RedisProperties redisProperties;

    @Value("${spring.redis.strategy}")
    private String strategy;


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getRedisFactory(jedisPoolConfig()));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(getRedisFactory(jedisPoolConfig()));
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration,SRM_CACHE);
    }


    /**
     * 连接池配置信息
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(redisProperties.getJedis().getPool() == null ? 100 : redisProperties.getJedis().getPool().getMaxActive());
        //最大空闲连接数
        jedisPoolConfig.setMaxIdle(redisProperties.getJedis().getPool() == null ? 100 : redisProperties.getJedis().getPool().getMaxIdle());
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(redisProperties.getJedis().getPool() == null ? 20 : redisProperties.getJedis().getPool().getMinIdle());
        //当池内没有可用连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool() == null ? 10000 : redisProperties.getJedis().getPool().getMaxWait().toMillis());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPoolConfig.setNumTestsPerEvictionRun(10);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1);
        return jedisPoolConfig;
    }


    //Redis 单点
    @Bean
    @ConditionalOnProperty(name="spring.redis.strategy", havingValue = "1")
    public JedisConnectionFactory getRedisFactory(JedisPoolConfig jedisPoolConfig) {
        log.info("load {}", "getRedisFactory");
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            configuration.setDatabase(redisProperties.getDatabase());
        }
        //获得默认的连接池构造
        //这里需要注意的是，edisConnectionFactoryJ对于Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
        //我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //修改我们的连接池配置
        jpcf.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();
        JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jedisClientConfiguration);
        return factory;
    }

    //Redis 集群配置
    @Bean
    @ConditionalOnProperty(name="spring.redis.strategy", havingValue = "2")
    public JedisConnectionFactory getClusterRedisFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory;
        log.info("load {}", "getClusterRedisFactory");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        for (String s :redisProperties.getCluster().getNodes()) {
            String[] split1 = s.split(":");
            redisClusterConfiguration.addClusterNode(new RedisNode(split1[0], Integer.parseInt(split1[1])));
        }
        //集群时最大重定向个数
        redisClusterConfiguration.setMaxRedirects(5);
        jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig );
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        return jedisConnectionFactory;
    }

    //Redis 哨兵
    @Bean
    @ConditionalOnProperty(name="spring.redis.strategy", havingValue = "3")
    public JedisConnectionFactory getSentinelRedisFactory(JedisPoolConfig jedisPoolConfig ) {
        JedisConnectionFactory jedisConnectionFactory;
        log.info("load {}", "getSentinelRedisFactory");
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        redisSentinelConfiguration.setMaster(redisProperties.getSentinel().getMaster());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            redisSentinelConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        List<String> sentinelArray = redisProperties.getSentinel().getNodes();
        for (String s : sentinelArray) {
            try {
                String[] split1 = s.split(":");
                redisSentinelConfiguration.addSentinel(new RedisNode(split1[0], Integer.parseInt(split1[1])));
            } catch (Exception e) {
                throw new RuntimeException(String.format("出现配置错误!请确认node=[%s]是否正确", s));
            }
        }
        jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration, jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public JedisSentinelPool jedisSentinelPool(JedisPoolConfig jedisPoolConfig){
        JedisSentinelPool jedisSentinelPool = null;
        if (SENTINEL == Integer.parseInt(strategy)) {
            Set<String> sentinels = new HashSet(redisProperties.getSentinel().getNodes());
            jedisSentinelPool = new JedisSentinelPool(redisProperties.getSentinel().getMaster(), sentinels,
                    jedisPoolConfig, redisProperties.getPassword());
        }
        return jedisSentinelPool;
    }

}
