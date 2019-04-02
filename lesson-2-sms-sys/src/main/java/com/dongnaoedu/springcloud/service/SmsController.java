package com.dongnaoedu.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 此处很重要 +++
public class SmsController {
	/**从 config服务端的 lesson-2-sms-sys.properties中获取内容，该properties文件名称必须与
	 * spring.application.name相同*/
	@Value("${tony.configString}")
	private String configString;

	/**提供 /test接口*/
	@RequestMapping("/test")
	public String sendSms() {
		return "configString的值：" + configString;
	}

	@Autowired
	Environment env;

	@RequestMapping("/get/{configName}")
	public String test(@PathVariable String configName) {
		return configName + "的值为：" + env.getProperty("tony." + configName);
	}
}
