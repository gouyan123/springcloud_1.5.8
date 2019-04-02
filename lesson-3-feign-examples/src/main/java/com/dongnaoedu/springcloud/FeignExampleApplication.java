package com.dongnaoedu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(defaultConfiguration=FeignClientsConfiguration.class)
public class FeignExampleApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(FeignExampleApplication.class).web(true).run(args);
	}
}
