/**
 * 
 */
package com.dongnaoedu.springcloud;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author zhang
 * @since v0.0.1 2017年9月8日 上午10:53:12
 */
@RestController
@RequestMapping("/timeout")
public class TestController {
	@RequestMapping("/test")
	public String timeOut() throws InterruptedException {
		Thread.sleep(3000L);
		return "ok";
	}

}
