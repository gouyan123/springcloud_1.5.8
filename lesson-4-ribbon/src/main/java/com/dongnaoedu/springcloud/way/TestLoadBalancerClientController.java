package com.dongnaoedu.springcloud.way;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// LoadBalancerClient的方式
@RestController
@RequestMapping("/loadbalance")
public class TestLoadBalancerClientController {
	static Logger logger = LoggerFactory.getLogger(TestLoadBalancerClientController.class);
	/**spring cloud 封装的 关于负载均衡组件 ribbon的工具类*/
	@Autowired
	LoadBalancerClient loadbalancerClient;

	// properties，针对ribbon的配置文件配置方式
	@RequestMapping("/properties")
	public void properties() {
		/**service-by-properties服务是 application.yml中配置的，选择该服务的一个实例*/
		ServiceInstance serviceInstance = loadbalancerClient.choose("service-by-properties");
		logger.warn("TestLoadBalancerClientController.properties执行结果：{}", serviceInstance.getUri());
	}

	// annotation，针对ribbon的启动类注解配置方式
	@RequestMapping("/annotation")
	public void annotation() {
		/**service-by-annotation服务是 启动类注解中配置的，选择该服务的一个实例*/
		ServiceInstance serviceInstance = loadbalancerClient.choose("service-by-annotation");
		logger.warn("TestLoadBalancerClientController.annotation执行结果：{}", serviceInstance.getUri());
	}

	// eureka，针对ribbon的eureka配置方式
	@RequestMapping("/eureka")
	public void eureka() {
		ServiceInstance serviceInstance = loadbalancerClient.choose("lesson-4-sms-interface");
		logger.warn("TestLoadBalancerClientController.eureka执行结果：{}", serviceInstance.getUri());
	}
}
