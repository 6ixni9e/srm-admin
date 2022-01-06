package com.guojin.srm.oss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createAllApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("SRMOSS").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createSysApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("系统管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.sys"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createCommonApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("基础公共接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.common"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createAuthApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("认证管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.auth"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createContractApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("合同管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.contract"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createOrgApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("机构管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.org"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createPaymentApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("付款管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.payment"))
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public Docket createPurchaseApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("采购管理接口").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.guojin.srm.oss.web.purchase"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("接口管理").description("接口描述")
				.contact(new Contact("bealon", "yuqian@yuqian.com", "www.yuqian.com")).version("1.0").build();
	}

}
