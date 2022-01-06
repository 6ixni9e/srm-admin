package com.guojin.srm.oss;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.guojin.srm")
@EnableDubbo
public class SRMAdminApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SRMAdminApplication.class, args);
		System.out.println("run");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SRMAdminApplication.class);
	}
}
