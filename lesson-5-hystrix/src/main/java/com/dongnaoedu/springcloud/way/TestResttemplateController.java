/**
 * 
 */
package com.dongnaoedu.springcloud.way;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

// resttemplate的方式
@RestController
@RequestMapping("/resttemplate")
@Configuration
public class TestResttemplateController {
	static Logger logger = LoggerFactory.getLogger(TestResttemplateController.class);

	@Bean
	@LoadBalanced // 这个注解一定要加，不然LoadBalancerAutoConfiguration不会对它进行处理
	RestTemplate RestTemplate() {
		return new RestTemplate();
	}

	@Autowired
	RestemplateSmsClient restemplateSmsClient;

	@RequestMapping("/timeout")
	public String timeout() {
		System.out.println("http请求timeout##########" + Thread.currentThread().toString());
		String body = restemplateSmsClient.timeout("参数时间:" + System.currentTimeMillis());
		return body;
	}

	@RequestMapping("/exception")
	public String exception() {
		System.out.println("http请求exception##########" + Thread.currentThread().toString());
		String body = restemplateSmsClient.exception();
		return body;
	}

}

// 和resttemplate集成时，需要使用hystrixCommand注解对应的方法
// @HystrixCommand注解中可配置的项和其他的使用方式一样
@Component
class RestemplateSmsClient {
	@Autowired
	RestTemplate restTemplate; // spring内置,封装了http请求的工具类

	@HystrixCommand(
			/**timeout()方法 调用其他服务 出现异常时，则调用 callTimeoutFallback方法*/
			fallbackMethod = "callTimeoutFallback",
			threadPoolProperties = {
					/**timeout()调用接口http://lesson-5-sms-interface/hystrix/timeout，同
					 * 一时刻只有一个调用成功，其他都默认调callTimeoutFallback方法*/
					@HystrixProperty(name = "coreSize", value = "1"),
					@HystrixProperty(name = "queueSizeRejectionThreshold", value = "1")
			},
			commandProperties = {
				/**timeout()调用接口http://lesson-5-sms-interface/hystrix/timeout，2s后没有
				 * 返回值，则请求超时，自动调用callTimeoutFallback方法*/
				@HystrixProperty(
						name = "execution.isolation.thread.timeoutInMilliseconds",
						value = "5000")
			}
	)
	public String timeout(String tempParam) {
		return restTemplate.getForObject("http://lesson-5-sms-interface/hystrix/timeout", String.class);
	}

	/**请求异常时，调用这个方法*/
	public String callTimeoutFallback(String tempParam) {
		return "这个是请求hystrix/timeout接口失败后的回调,加上一个没什么卵用的参数： " + tempParam ;
	}

	
	
	@HystrixCommand(fallbackMethod = "callExceptionFallback")
	public String exception() {
		return restTemplate.getForObject("http://lesson-5-sms-interface/hystrix/exception", String.class);
	}

	// 这个是请求接口失败后的回调
	public String callExceptionFallback() {
		return "这个是请求hystrix/exception接口失败后的回调";
	}

	/**
	 * 实际上，任意的方法上面我们都可以使用hystrixCommand进行注解，将这个方法包装成一个hystrix的命令去执行。
	比如我们可以在数据库操作上面加上这个注解，来实现数据库操作的熔断。
	 */
	 @HystrixCommand
	 public String doSome() {
	 // TODO 这里可以写数据库操作， 写redis操作等等...都可以使用hystrixCommand进行封装。
		 return null;
	 }

}