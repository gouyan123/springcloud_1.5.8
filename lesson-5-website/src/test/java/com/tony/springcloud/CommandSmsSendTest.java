package com.tony.springcloud;

import java.io.InputStream;
import java.net.URL;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandSmsSendTest extends HystrixCommand<String> {

	protected CommandSmsSendTest() {
		super(HystrixCommandGroupKey.Factory.asKey("smsGroup"));
	}

	/**
	 * 封装调用的代码
	 */
	@Override
	protected String run() throws Exception {

		URL url = new URL("http://localhost:9001/send/tony");
		byte[] result = new byte[3];
		InputStream input = url.openStream();
		IOUtils.readFully(input, result);

		String content = new String(result);
		return content;
	}
	
	@Override
	protected String getFallback() {
		// 这里就是执行我们的降级策略
		return "触发降级策略，当服务不可用时，返回此处执行的结果。cache/备用接口/mock值";
	}

}
