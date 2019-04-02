package com.dongnaoedu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

import com.dongnaoedu.springcloud.javaconfig.ServiceByAnnontationConfiguration;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
// 通过注解的方式定义了一个针对service-by-annotation服务的负载均衡器
@RibbonClients(value = {
		@RibbonClient(name = "service-by-annotation", configuration = ServiceByAnnontationConfiguration.class) })
public class RibbonSampleApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(RibbonSampleApplication.class).web(true).run(args);
	}

	// 这里定义 可以覆盖所有的
	// @Bean
	// public IRule ribbonRule() {
	// return new RandomRule();
	// }

}
