package com.dongnaoedu.springcloud.protogenesis;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import feign.Client;
import feign.Contract;
import feign.Request;
import feign.httpclient.ApacheHttpClient;

public class SmsClientConfiguration {
	
	// 自定义http请求的工具
	@Bean
	Client apacheHttpClient() {
		return new ApacheHttpClient();
	}
	
	// 自定义请求超时时间
	@Bean
	Request.Options feignOptions() {
		return new Request.Options(1 * 1000, 1 * 1000);
	}

	// 自定义参数解析器
	@Bean
	public Contract feignContract(ConversionService feignConversionService) {
		return new feign.Contract.Default();
	}

}