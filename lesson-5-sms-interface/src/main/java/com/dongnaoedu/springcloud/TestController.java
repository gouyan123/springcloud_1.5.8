/**
 * 
 */
package com.dongnaoedu.springcloud;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hystrix")
public class TestController {

	/**模拟调用接口，响应超时*/
	@ResponseBody
	@RequestMapping("/timeout")
	public String timeOut() throws InterruptedException {
		System.out.println("有人请求timeout啦...，当前时间:" + System.currentTimeMillis());
		// 睡眠3秒，模拟超时
		Thread.sleep(5000L);
		return "ok";
	}

	/**模拟网络不稳定，出现报错，偶尔失败偶尔失败*/
	@RequestMapping("/exception")
	public String exception(HttpServletResponse servletResponse) throws InterruptedException, IOException {
		System.out.println("有人请求exception接口啦...，当前时间:" + System.currentTimeMillis());
		if (new Random().nextInt(1000) % 2 == 0) {
			// 随机设置响应状态码为503，服务不可用的状态
			servletResponse.setStatus(503);
			servletResponse.getWriter().write("error");
			servletResponse.flushBuffer();
		} else {
			// 返回正常
			servletResponse.setStatus(200);
			servletResponse.getWriter().write("ok");
			servletResponse.flushBuffer();
		}
		return "";
	}

}
