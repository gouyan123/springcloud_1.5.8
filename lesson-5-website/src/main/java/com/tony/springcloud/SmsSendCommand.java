package com.tony.springcloud;

import java.net.URL;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class SmsSendCommand extends HystrixCommand<String> {

	protected SmsSendCommand() {
		// java代码配置， 只针对这个命令
		super(Setter
				/**必填，指定命令分组名，用于统计；一个服务多个接口，分组命名与服务名一致，无特别意义；*/
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("smsGroup"))
				/**依赖名称，如果是服务调用，写具体的接口名，如果是自定义的操作，就自己命名；默认是command实现类的类名；熔断配置就是根据这个名称*/
				.andCommandKey(HystrixCommandKey.Factory.asKey("smsCommandKey"))
				/**线程池命名，默认是HystrixCommandGroupKey的名称，线程池配置就是根据这个名称；此处 显示配置为smsThreadPool，没有使用默认名*/
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("smsThreadPool"))
				/**以上配置 告诉hystrix，命令在哪个线程池执行*/
				/*command 熔断相关参数配置*/
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
				// 配置隔离方式：默认采用线程池隔离。还有一种信号量隔离方式,
				// .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
				// 超时时间500毫秒
				// .withExecutionTimeoutInMilliseconds(500)
				// 信号量隔离的模式下，最大的请求数。和线程池大小的意义一样
				// .withExecutionIsolationSemaphoreMaxConcurrentRequests(2)
				// 熔断时间（熔断开启后，各5秒后进入半开启状态，试探是否恢复正常）
				// .withCircuitBreakerSleepWindowInMilliseconds(5000)
				)
				/**设置线程池大小*/
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						/**设置线程池大小；此处表示该线程池只能存一个任务，同一时刻只能将一个http请求映射到run()方法进行具体操作，其他http请求都映射
						 * 到getFallback()方法，进行降级处理*/
						.withCoreSize(1)));
	}

	/**website[服务调用者]创建一个线程任务 调用sms-interface[服务提供者]的 /hystrix/timeout和/hystrix/exception接口，sms-interface[服务提供者]
	 * 使用线程池接收该 线程任务，通过设置sms-interface[服务提供者]线程池大小，设置并发访问数量*/
	@Override
	protected String run() throws Exception {
		System.out.println("###command#######" + Thread.currentThread().toString());
		// 请求一个需要3秒才能得到响应的接口，用来模拟业务处理场景
		 URL url = new URL("http://localhost:9002/hystrix/timeout");
		// 请求一个会随机报错的接口，用来做熔断的演示
//		URL url = new URL("http://localhost:9002/hystrix/exception");
		byte[] result = new byte[1024];
		url.openStream().read(result);
		System.out.println("###command结束#######" + Thread.currentThread().toString() + ">><>>>执行结果:" + new String(result));
		return new String(result);
	}
	/**每个http请求对应一个线程池，当http请求数量大于线程池容量时，http请求映射到 getFallback()方法，进行降级处理*/
	@Override
	protected String getFallback() {
		System.out.println("###降级啦####" + Thread.currentThread().toString());
		return "降级";
	}

}
