package com.dongnaoedu.springcloud.service.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import redis.clients.jedis.JedisPool;

@Configuration
@EnableAutoConfiguration
@PropertySource(value = { "classpath:application.properties" })
@Import(value = { ConfigServiceBootstrapConfiguration.class })
@ImportResource("classpath:spring-servlet.xml")
public class WebConfig {

	@Bean
	public PropertySourceBootstrapConfiguration propertySourceBootstrapConfiguration(
			ConfigurableApplicationContext configurableApplicationContext) {
		PropertySourceBootstrapConfiguration propertySourceBootstrapConfiguration = configurableApplicationContext
				.getBeanFactory().createBean(PropertySourceBootstrapConfiguration.class);
		propertySourceBootstrapConfiguration.initialize(configurableApplicationContext);
		return propertySourceBootstrapConfiguration;
	}
	
	@Autowired
	Environment env;

	@Bean
	@RefreshScope
	public JedisPool jedisPool() {
		String host = env.getProperty("redis.host");
		int port = env.getProperty("redis.port", Integer.class);
		return new JedisPool(host, port);
	}

}
