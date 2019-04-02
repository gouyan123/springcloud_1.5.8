package com.dongnaoedu.springcloud.mvc;

import com.dongnaoedu.springcloud.FeignExampleApplication;
import com.dongnaoedu.springcloud.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign2")
public class TestController2 {
	static Logger logger = LoggerFactory.getLogger(FeignExampleApplication.class);

	@Autowired
	FeignSmsClient feignSmsClient;

	// get 请求
	@RequestMapping("/test1")
	public String test1() {
		String body = feignSmsClient.querySms();
		return body;
	}
	
	// post 请求
	@RequestMapping("/test2")
	public String test2(@RequestBody Sms sms) {
		String body = feignSmsClient.sendSms(sms);
		return body;
	}
	
	@RequestMapping("/test4")
	public Sms test4(String id) {
		Sms sendSmsTemplate = feignSmsClient.getSms(id);
		return sendSmsTemplate;
	}
	
	@RequestMapping("/test5")
	public String test5() {
		String sendSmsTemplate = feignSmsClient.fail404();
		return sendSmsTemplate;
	}
	
	@RequestMapping("/test6")
	public String test6() {
		String sendSmsTemplate = feignSmsClient.timeOut();
		return sendSmsTemplate;
	}
}
