package com.dongnaoedu.springcloud.service.member.web;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/mvc")
public class EurekaSmsController {

	@Autowired
	DiscoveryClient discoveryClient;

	@RequestMapping("/test")
	public String sendSms(String content) throws Exception {
		// 非 配置 ip 端口
		// 通过服务名获取实例
		List<ServiceInstance> instances = discoveryClient.getInstances("lesson-1-sms-interface");
		ServiceInstance serviceInstance = instances.get(0);

		String host = serviceInstance.getHost();
		int port = serviceInstance.getPort();

		String result = new RestTemplate().getForObject("http://" + host + ":" + port + "/sms/1", String.class);

		return result;
	}
}