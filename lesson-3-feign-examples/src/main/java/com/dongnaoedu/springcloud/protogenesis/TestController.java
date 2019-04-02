package com.dongnaoedu.springcloud.protogenesis;

import com.dongnaoedu.springcloud.FeignExampleApplication;
import com.dongnaoedu.springcloud.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign1")
public class TestController {
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
	
	@RequestMapping("/test3")
	public String test3(String phone, String content) {
		String sendSmsTemplate = feignSmsClient.sendSmsTemplate(phone, content);
		return sendSmsTemplate;
	}
	
//	@RequestMapping("/test4")
//	public Sms test4(String id) {
//		Sms sendSmsTemplate = feignSmsClient.getSms(id);
//		return sendSmsTemplate;
//	}
	
	@RequestMapping("/test5")
	public String test5(String id) {
		String string = feignSmsClient.timeOut();
		return string;
	}
	
}
