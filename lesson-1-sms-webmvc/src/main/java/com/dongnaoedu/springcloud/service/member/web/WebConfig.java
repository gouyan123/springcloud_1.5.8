package com.dongnaoedu.springcloud.service.member.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@PropertySource(value = { "classpath:application.properties" })
@Import(value = { EurekaDiscoveryClientConfiguration.class,EurekaClientAutoConfiguration.class })
@EnableEurekaClient
@ImportResource("classpath:spring-servlet.xml")
public class WebConfig {

}
