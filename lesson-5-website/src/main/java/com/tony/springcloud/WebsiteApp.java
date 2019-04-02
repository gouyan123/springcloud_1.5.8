package com.tony.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WebsiteApp {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(WebsiteApp.class).web(true).run(args);
	}
}
