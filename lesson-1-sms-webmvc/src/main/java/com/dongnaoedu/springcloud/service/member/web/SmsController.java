package com.dongnaoedu.springcloud.service.member.web;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

	@Value("${sms.host}")
	private String host;

	@Value("${sms.port}")
	private String port;

	@RequestMapping("/test")
	public String sendSms(String content) throws Exception {

		URL url = new URL("http://" + host + ":" + port + "/sms/send");
		byte[] result = new byte[512];
		url.openStream().read(result);

		return new String(result);
	}
}