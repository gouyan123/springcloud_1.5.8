
package com.dongnaoedu.springcloud.javaconfig;

import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

// 注解的方式，对应RibbonSampleApplication中@RibbonClients
public class ServiceByAnnontationConfiguration {
	// service-by-annotation服务的实例源
	@Bean
	public ServerList<Server> ribbonServerList() {
		// 实例列表
		String listOfServers = "http://www.csdn.net,http://www.baidu.com,http://www.dongnaoedu.com";
		String[] splits = listOfServers.split(",");
		int len = splits.length;
		if (len == 0) {
			return new StaticServerList<Server>();
		}

		Server[] servers = new Server[len];
		for (int i = 0; i < len; i++) {
			servers[i] = new Server(splits[i].trim());
		}
		// 返回这个...静态的
		return new StaticServerList<Server>(servers);
	}
	
	// 负载策略
	@Bean
	public IRule iniRule() {
		 // 轮询
//		 return new RoundRobinRule();

		// 随机
			return new RandomRule();
	}
}
