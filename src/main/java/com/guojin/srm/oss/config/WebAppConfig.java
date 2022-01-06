package com.guojin.srm.oss.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.guojin.srm.common.constant.CommonConstants;
import com.guojin.srm.common.redis.Json2ParamsHandlerMethodArgumentResolver;
import com.guojin.srm.oss.interceptor.OssHeaderCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//oemCode机构编码检查(除获取OEM机构编码接口外请求头均需要OEM机构编码)，超时时会丢失
		List<String> excludeList = new ArrayList<>();
		excludeList.add("/sys/manager/queryOemCode");
		excludeList.add("/swagger-resources/**");
		excludeList.add("/swagger-ui.html");
		excludeList.add("/v2/**");
		registry.addInterceptor(ossHeaderCheckInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeList);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new Json2ParamsHandlerMethodArgumentResolver());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "OPTIONS")
				.allowCredentials(true)
				.exposedHeaders("X-AUTH-SESSION","Content-Security-Policy","X-Content-Type-Options","X-XSS-Protection")
				.allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "X-AUTH-SESSION","Content-Security-Policy","X-Content-Type-Options","X-XSS-Protection", CommonConstants.TOEKN,CommonConstants.OEM_CODE)
				.maxAge(3600);
		
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(fastJsonHttpMessageConverter());
	}

	@Bean
	@SuppressWarnings("all")
	public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat, 
				SerializerFeature.WriteNullStringAsEmpty, 
		        SerializerFeature.WriteNullListAsEmpty,
		        SerializerFeature.WriteMapNullValue,
		        SerializerFeature.WriteDateUseDateFormat,
		        SerializerFeature.WriteNullBooleanAsFalse
				);
		// 处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);

		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return fastConverter;
	}

	@Bean
	public OssHeaderCheckInterceptor ossHeaderCheckInterceptor(){
		return new OssHeaderCheckInterceptor();
	}

}
