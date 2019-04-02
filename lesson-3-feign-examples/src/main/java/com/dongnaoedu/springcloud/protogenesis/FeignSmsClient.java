package com.dongnaoedu.springcloud.protogenesis;

//import javax.ws.rs.PathParam;

import com.dongnaoedu.springcloud.Sms;
import org.springframework.cloud.netflix.feign.FeignClient;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

// 使用原生的feign注解
@FeignClient(name = "lesson-3-sms-interface-1", url = "http://localhost:9002", configuration = SmsClientConfiguration.class)
public interface FeignSmsClient {

	/** 普通 */
	@RequestLine("GET /sms")
	String querySms();

	/** 传递对象 */
	@RequestLine("POST /sms")
	String sendSms(Sms sms);

	/** 固定一个body请求的模板，占位符代表参数 */
	@RequestLine("POST /sms")
	@Headers("Content-Type: application/json")
	// 可以是{"pohone":"10086","content":"hahha"}
	// 或者可以是<xml></xml>
	@Body("%7B\"phone\": \"{phone}\", \"content\": \"{content}\"%7D")
	String sendSmsTemplate(@Param("phone") String phone, @Param("content") String content);

	/** 获取短信内容 */
//	@RequestLine("GET /sms/{id}")
//	Sms getSms(@PathParam("id") String id);

	@RequestLine("GET /timeout/test")
	String timeOut();

	
}
