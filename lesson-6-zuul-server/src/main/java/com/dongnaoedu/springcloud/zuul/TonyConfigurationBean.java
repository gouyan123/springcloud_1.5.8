package com.dongnaoedu.springcloud.zuul;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tony.zuul.tokenFilter")		// spring会将tony.zuul.tokenFilter开头的配置项 解析进来
public class TonyConfigurationBean {
	// 这个列表存的是routeId，列表里面的路由，不需要进行token校验，在TokenValidataFilter中会用到
	private List<String> noAuthenticationRoutes;

	public List<String> getNoAuthenticationRoutes() {
		return noAuthenticationRoutes;
	}

	public void setNoAuthenticationRoutes(List<String> noAuthenticationRoutes) {
		this.noAuthenticationRoutes = noAuthenticationRoutes;
	}

	
}
