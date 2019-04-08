package com.dongnaoedu.springcloud.mvc;

import com.dongnaoedu.springcloud.Sms;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*必须引入hytrix, 才会生效fallback = FeignSmsMvcClientFailback.class
不配置fallback，出错了就会直接返回错误信息*/
			/**该接口调用lesson-3-sms-interface服务*/
@FeignClient(name = "lesson-3-sms-interface", fallback = FeignSmsMvcClientFailback.class)
@Service
public interface FeignSmsClient {
	/**调用lesson-3-sms-interface服务的/sms接口，即发送GET请求http:lesson-3-sms-interface/sms*/
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	String querySms();

	/** 传递对象 @RequestBody 表示post请求 Body中的数据*/
	// 实际上是对象转json，作为请求报
	/**调用lesson-3-sms-interface服务的/sms接口，即发送POST请求http:lesson-3-sms-interface/sms及JSON数据，该JSON数据被Sms接收*/
	@RequestMapping(value = "/sms", method = RequestMethod.POST)
	String sendSms(@RequestBody Sms sms);

	/**调用lesson-3-sms-interface服务的/sms接口，即发送GET请求http:lesson-3-sms-interface/sms/id id为路径参数*/
	@RequestMapping(value = "/sms/{id}", method = RequestMethod.GET)
	Sms getSms(@PathVariable("id") String id);

	/** 这是一个会报错的请求，测试fallback */
	@RequestMapping(value = "/fail404", method = RequestMethod.GET)
	String fail404();

	@RequestMapping(value = "/timeout/test", method = RequestMethod.GET)
	String timeOut();

}
