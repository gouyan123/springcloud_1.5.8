package com.dongnaoedu.springcloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 服务提供者 - 短信接口
@SpringBootApplication
@EnableEurekaClient
/**扫描该包下的所有@Repositoriy注解*/
@EnableJpaRepositories("com.dongnaoedu.springcloud")
public class SmsInterfaceApp {
	public static void main(String[] args) {
		SpringApplication.run(SmsInterfaceApp.class,args);
	}
}
