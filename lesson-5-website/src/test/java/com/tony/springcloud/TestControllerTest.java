package com.tony.springcloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class TestControllerTest {

	public String test() throws IOException{

		URL url = new URL("http://localhost:9001/send/tony");
		byte[] result = new byte[3];
		InputStream input = url.openStream();
		IOUtils.readFully(input, result);
		
		System.out.println(new String(result));
		
		return new String(result);
	}
	
	public String testHystrix() throws IOException{
		String result = new CommandSmsSendTest().execute();
		return result;
	}
	

	public String hello() throws IOException{
		
		return "world";
	}
}
