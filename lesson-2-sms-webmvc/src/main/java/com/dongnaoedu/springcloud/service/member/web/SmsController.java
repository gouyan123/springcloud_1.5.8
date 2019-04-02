package com.dongnaoedu.springcloud.service.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.JedisPool;
// ÆÕÍ¨µÄspring mvc 
@RestController
@RequestMapping("/sms")
@RefreshScope
public class SmsController {

	@Value("${tony.configString}")
	private String configString;

	@Autowired
	JedisPool jedisPool;
	
	@RequestMapping("/send")
	public String sendSms(String content) {
		String value = jedisPool.getResource().get("a");
		return configString + " >> redis value:" + value;
	}
}