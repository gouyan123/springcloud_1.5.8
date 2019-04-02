package com.dongnaoedu.springcloud.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**启动SpringBoot，相当于将web项目发布到tomcat中，并启动tomcat*/
@SpringBootApplication
/**启动 config服务端*/
@EnableConfigServer
/**启动eureka客户端，作为服务提供者，还是消费者，取决于application.yml中eureka.client的配置*/
@EnableEurekaClient
public class ConfigServerApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ConfigServerApplication.class).web(true).run(args);
	}
}
