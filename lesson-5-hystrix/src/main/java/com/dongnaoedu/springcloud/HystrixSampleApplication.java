package com.dongnaoedu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker	// 启用熔断机制
@EnableHystrix
@EnableEurekaClient
public class HystrixSampleApplication {
	public static void main(String[] args) {
//		new SpringApplicationBuilder(HystrixSampleApplication.class).web(true).run(args);
		SpringApplication.run(HystrixSampleApplication.class);
	}
}
