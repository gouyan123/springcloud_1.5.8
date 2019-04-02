package com.dongnaoedu.springcloud.uaa;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.dongnaoedu.springcloud")
@EnableDiscoveryClient
public class UAAApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(UAAApplication.class).web(true).run(args);
	}
}
