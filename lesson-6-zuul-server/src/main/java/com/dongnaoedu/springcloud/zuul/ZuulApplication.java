package com.dongnaoedu.springcloud.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient				// 开启eureka客户端
@EnableZuulProxy				// 开启网关
@EnableCircuitBreaker			// 开启熔断
@ComponentScan("com.dongnaoedu.springcloud")
public class ZuulApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
	}
}
