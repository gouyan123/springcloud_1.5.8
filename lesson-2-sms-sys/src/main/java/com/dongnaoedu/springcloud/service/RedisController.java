package com.dongnaoedu.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@Configuration
@RestController
@RequestMapping("/redis")
public class RedisController {
	@Autowired
	Environment env;

	@Bean
	/**@RefreshScope 将该方法定义为刷新范围，当refresh刷新配置时，JedisPool这个实例会被刷新*/
	@RefreshScope
	public JedisPool jedisPool() {
		String host = env.getProperty("redis.host");
		int port = env.getProperty("redis.port", Integer.class);
		return new JedisPool(host, port);
	}

	@Autowired
	private JedisPool jedisPool;

	/*获取一个key的值 并打印出来*/
	@RequestMapping("/get/{key}")
	public String sendSms(@PathVariable String key) {
		String value = jedisPool.getResource().get(key);
		return "	redis中" + key + "的值为:" + value;
	}
}
