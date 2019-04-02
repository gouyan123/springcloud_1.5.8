package com.tony.springcloud;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
	
	@Autowired
	DiscoveryClient discoveryClient;// spring cloud 提供的客户端工具
	
	@RequestMapping("test")
	public String test() throws IOException{
		// 调用服务器之前，通过eureka client 查询服务的实例信息
		// 也就是说去看 部署了哪些机器，以及IP 端口等信息
		List<ServiceInstance> instances = discoveryClient.getInstances("lesson-1-sms-interface");
		
		for (ServiceInstance serviceInstance : instances) {
			System.out.println("获取到服务实例：" + serviceInstance.getPort());
		}
		// ribbon 客户端负载均衡
		ServiceInstance serviceInstance = instances.get(0);
		
		// 获取的IP  端口  信息
		String host = serviceInstance.getHost();
		int port = serviceInstance.getPort();

		String result = new RestTemplate().getForObject("http://" + host + ":" + port +"/sms/1", String.class);
		
		return result;
	}
}
