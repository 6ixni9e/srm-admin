package com.guojin.srm.oss.config;

import com.guojin.srm.common.filter.ApiAuthenticationFilter;
import com.guojin.srm.common.redis.JdkRedisSerializer;
import com.guojin.srm.common.shiro.*;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.properties.SysConfigProperties;
import com.guojin.srm.oss.shiro.UserNamePassWordRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.Filter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private SysConfigProperties sysConfigProperties;


    @Bean("shiroRedisConnectionFactory")
    public RedisConnectionFactory shiroRedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(10);
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        if (StringUtil.isNotEmpty(redisProperties.getPassword())) {
            jedisConnectionFactory.setPassword(redisProperties.getPassword());
        }
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setTimeout(3000);
        jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
        return jedisConnectionFactory;
    }

    @Bean("shiroRedisSessionRedisTemplate")
    public RedisTemplate<String, Session> shiroRedisSessionRedisTemplate() {
        RedisTemplate<String, Session> sessionRedisTemplate = new RedisTemplate<>();
        sessionRedisTemplate.setConnectionFactory(shiroRedisConnectionFactory());
        JdkRedisSerializer<Serializable> collectionSerializer = JdkRedisSerializer.getInstance();
        sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
        sessionRedisTemplate.setValueSerializer(collectionSerializer);
        sessionRedisTemplate.afterPropertiesSet();
        return sessionRedisTemplate;
    }

    @Bean("shiroRedisCacheRedisTemplate")
    public RedisTemplate<Object, Serializable> shiroRedisCacheRedisTemplate() {
        RedisTemplate<Object, Serializable> cacheRedisTemplate = new RedisTemplate<>();
        cacheRedisTemplate.setConnectionFactory(shiroRedisConnectionFactory());
        cacheRedisTemplate.afterPropertiesSet();
        return cacheRedisTemplate;
    }

    @Bean
    public SessionDAO sessionDAO() {
        ShiroRedisSessionDAO sessionDAO = new ShiroRedisSessionDAO();
        sessionDAO.setRedisSessionTimeout(sysConfigProperties.getSessionTimeout());
        sessionDAO.setKeyPrefix("srmoss");
        return sessionDAO;
    }

    @Bean
    public CacheManager cacheManagers() {
        ShiroRedisCacheManager shiroRedisCacheManager = new ShiroRedisCacheManager();
        shiroRedisCacheManager.setExpire(sysConfigProperties.getSessionTimeout());
        shiroRedisCacheManager.setKeyPrefix("srmoss");
        return shiroRedisCacheManager;
    }

    @Bean
    protected SessionManager sessionManager() {
        HeaderWebSessionManager sessionManager = new HeaderWebSessionManager();
        sessionManager.setSessionIdCookieEnabled(false);
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setGlobalSessionTimeout(1000 * sysConfigProperties.getSessionTimeout());
        return sessionManager;
    }

    @Bean
    protected SessionsSecurityManager securityManager(AuthorizingRealm authorizingRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManagers());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(authorizingRealm);
        return securityManager;
    }

    @Bean
    public AuthorizingRealm authorizingRealm() {
        UserNamePassWordRealm userRealm = new UserNamePassWordRealm();
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        return userRealm;
    }

    @Bean
    protected ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setLoginUrl(sysConfigProperties.getLoginUrl());
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        TestAuthenticationFilter testAuthenticationFilter = new TestAuthenticationFilter();
        TestAuthenticationFilter.setAllowOrigins(sysConfigProperties.getAllowOrigins());
        TestAuthenticationFilter.setTestEnvironment(sysConfigProperties.isTestEnvironment());

        ApiAuthenticationFilter apiAuthenticationFilter = new ApiAuthenticationFilter();

        filters.put("test", testAuthenticationFilter);
        filters.put("api", apiAuthenticationFilter);

        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/statics/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/favicon.ico", "anon");
//        filterMap.put("/api/dynamiccode/loginCode", "anon");
//        filterMap.put("/api/dynamiccode/forgetPwd", "anon");
        filterMap.put("/api/dynamiccode/*", "anon");
//        filterMap.put("/sys/manager/login", "anon");
//        filterMap.put("/sys/manager/forgetPassword", "anon");
//        filterMap.put("/sys/manager/logout", "anon");
        filterMap.put("/sys/manager/*", "anon");
//        filterMap.put("/oem/manage/getByOemCode", "anon");

        filterMap.put("/image/**", "test");
        filterMap.put("/v2/api-docs", "test");
        filterMap.put("/webjars/**", "test");

        filterMap.put("/**", "api");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("advisorAutoProxyCreator")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public TestAopAllianceAnnotationsAuthorizingMethodInterceptor apiAopAllianceAnnotationsAuthorizingMethodInterceptor() {
        TestAopAllianceAnnotationsAuthorizingMethodInterceptor aopAllianceAnnotationsAuthorizingMethodInterceptor = new TestAopAllianceAnnotationsAuthorizingMethodInterceptor();
        aopAllianceAnnotationsAuthorizingMethodInterceptor.setTestEnvironment(sysConfigProperties.isTestEnvironment());
        return aopAllianceAnnotationsAuthorizingMethodInterceptor;
    }

    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        advisor.setAdvice(apiAopAllianceAnnotationsAuthorizingMethodInterceptor());
        return advisor;
    }

}
