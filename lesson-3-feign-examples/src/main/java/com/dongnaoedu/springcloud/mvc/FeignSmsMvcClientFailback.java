/**
 * 
 */
package com.dongnaoedu.springcloud.mvc;

import com.dongnaoedu.springcloud.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** 服务降级的一种手段 - 备用接口或mock接口  */
// 当接口请求出错了，就会调用这里面的方法。
@Component
public class FeignSmsMvcClientFailback implements FeignSmsClient{

	static final Logger logger = LoggerFactory.getLogger(FeignSmsMvcClientFailback.class);
	
	@Override
	public String querySms() {
		logger.warn("调用querySms出错");
		return "querySms出错啦";
	}

	@Override
	public String sendSms(Sms sms) {
		logger.warn("调用sendSms出错");
		return "sendSms出错啦";
	}
	
	@Override
	public String fail404() {
		logger.warn("调用fail404出错");
		return "fail404出错啦";
	}

	@Override
	public Sms getSms(String id) {
		logger.warn("调用sendSms出错");
		return new Sms("这是错误的","这是错误的");
	}
	
	@Override
	public String timeOut() {
		return "调用timeOut出错";
	}

}
