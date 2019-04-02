/**
 * 
 */
package com.dongnaoedu.springcloud.way;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// Feign的方式
@RestController
@RequestMapping("/feign")
public class TestFeignController {
	static Logger logger = LoggerFactory.getLogger(TestFeignController.class);
	// properties
	@Autowired
	FeignPropertiesClient feignPropertiesClient;

	@RequestMapping("/properties")
	public void properties() {
		String body = feignPropertiesClient.index();
		testprint(body);// 仅仅是为了根据输出内容来判断是调用的哪个接口
	}

	// annotation
	@Autowired
	FeignAnnotationClient feignAnnotationClient;

	@RequestMapping("/annotation")
	public void annotation() {
		String body = feignAnnotationClient.index();
		testprint(body);// 仅仅是为了根据输出内容来判断是调用的哪个接口
	}

	// eureka
	@Autowired
	FeignEurekaClient feignEurekaClient;

	@RequestMapping("/eureka")
	public void eureka() {
		String body = feignEurekaClient.index();
		logger.warn("TestFeignController.eureka执行结果：{}", body);
	}

	// 仅仅是为了根据输出内容来判断是调用的哪个接口
	private void testprint(String body) {
		if (body.contains("dongnaoedu")) {
			logger.warn("根据负载均衡策略，选择实例:dongnaoedu.com");
		} else if (body.contains("csdn")) {
			logger.warn("根据负载均衡策略，选择实例:csdn.net");
		} else if (body.contains("baidu")) {
			logger.warn("根据负载均衡策略，选择实例:baidu.com");
		} else {
			logger.warn("火星....");
		}
	}
}

@FeignClient(name = "service-by-properties")
interface FeignPropertiesClient {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index();
}

@FeignClient(name = "service-by-annotation")
interface FeignAnnotationClient {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index();
}

@FeignClient(name = "lesson-4-sms-interface")
interface FeignEurekaClient {
	@RequestMapping(value = "/timeout/test", method = RequestMethod.GET)
	public String index();

}