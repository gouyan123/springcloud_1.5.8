package com.tony.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class WebsiteAppTest {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(WebsiteApp.class).web(true).run(args);
	}
}
