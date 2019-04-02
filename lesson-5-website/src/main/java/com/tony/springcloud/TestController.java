package com.tony.springcloud;

import java.io.IOException;
import java.net.URL;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping("test")
	public String test() throws IOException{
		// 普通方式调用接口，不管接口稳不稳定，会一直请求，占用服务器资源，造成雪崩
		URL url = new URL("http://localhost:9002/hystrix/timeout");
		byte[] result = new byte[1024];
		url.openStream().read(result);
		return new String(result);
	}
	
	@RequestMapping("test_hystrix")
	public String test_hystrix() throws IOException{
		// 通过hystrix调用接口，如果接口不稳定，会开启熔断，防止雪崩
		// 同步调用
		System.out.println("http请求##########"+Thread.currentThread().toString());
		return new SmsSendCommand().execute();
	}

	@RequestMapping("hello")
	public String hello() throws IOException{
		return "world";
	}
}
