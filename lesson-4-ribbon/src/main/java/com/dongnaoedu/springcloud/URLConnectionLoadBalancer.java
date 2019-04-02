package com.dongnaoedu.springcloud;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;
import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.LoadBalancerStats;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;

import rx.Observable;

// 官网示例
public class URLConnectionLoadBalancer {
   
    private final ILoadBalancer loadBalancer;
    // 重试机制，同一个实例不进行重试，最多重试一次
    private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);
    
    public URLConnectionLoadBalancer(List<Server> serverList) {
    	// 使用构造器创建一个负载均衡器
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
    }
    
    public String call(final String path) throws Exception {
    	//  rxjava 响应式编程
        return LoadBalancerCommand.<String>builder()
                .withLoadBalancer(loadBalancer)
                .build()
                .submit(new ServerOperation<String>() {
            @Override
            public Observable<String> call(Server server) {
                URL url;
                try {
                    url = new URL("http://" + server.getHost() + ":" + server.getPort() + path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    return Observable.just(conn.getResponseMessage());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).toBlocking().first();
    }
    
    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }

    public static void main(String[] args) throws Exception {
    	// 构建一个负载均衡器， 固定三个server实例
        URLConnectionLoadBalancer urlLoadBalancer = new URLConnectionLoadBalancer(Lists.newArrayList(
                new Server("www.baidu.com", 80),
                new Server("www.oschina.net", 80),
                new Server("www.taobao.com", 80)));
        // 调用6次
        for (int i = 0; i < 6; i++) {
            System.out.println(urlLoadBalancer.call("/"));
        }
        // 输出负载均衡器记录的信息
        System.out.println("=== Load balancer stats ===");
        System.out.println(urlLoadBalancer.getLoadBalancerStats());
    }
}