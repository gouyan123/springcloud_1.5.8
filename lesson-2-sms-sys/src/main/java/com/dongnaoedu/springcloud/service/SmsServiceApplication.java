package com.dongnaoedu.springcloud.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**避免springboot自己默认创建 单数据源*/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.dongnaoedu")
@EnableEurekaClient
@MapperScan("com.dongnaoedu.springcloud.service.mybatis.dao1")
/**没有启动 config客户端的注解*/
public class SmsServiceApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(SmsServiceApplication.class).web(true).run(args);
	}
}
