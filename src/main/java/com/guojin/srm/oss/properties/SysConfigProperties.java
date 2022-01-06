package com.guojin.srm.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "sys.config")
@Getter
@Setter
public class SysConfigProperties {

	/**
	 * 添加用户缺省的机构类型
	 */
	private Long defaultPartyId;

	/**
	 * 超级管理员，默认二级目录
	 */
	private List<Long> sysMenuIds;

	/**
	 * 跨域域名
	 */
	private List<String> allowOrigins;

	/**
	 * 登录首页url
	 */
	private String loginUrl;

	/**
	 * 接口验权(true不验权)
	 */
	private boolean testEnvironment = true;

	/**
	 * session有效期（单位：秒）
	 */
	private Integer sessionTimeout;

	/**
	 * 单点登录开关
	 */
	private boolean singleLoginSwitch;

}
