package com.dongnaoedu.springcloud.way;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import feign.hystrix.FallbackFactory;

// Feign的方式
@RestController
@RequestMapping("/feign")
public class TestFeignController {
	static Logger logger = LoggerFactory.getLogger(TestFeignController.class);

	@Autowired
	FeignSmsClient feignSmsClient;

	@RequestMapping("/timeout")
	public String timeout() {
		System.out.println("http请求timeout##########" + Thread.currentThread().toString());
		String body = feignSmsClient.timeout("参数时间:" + System.currentTimeMillis());
		return body;
	}

	@RequestMapping("/exception")
	public String exception() {
		System.out.println("http请求exception##########" + Thread.currentThread().toString());
		String body = feignSmsClient.exception();
		return body;
	}
}

/**当调用timeout()方法时，请求lesson-5-sms-interface服务的/hystrix/timeout接口，SmsFallBackFactory
 * 实现当前FeignSmsClient接口，实现方法里面配置降级方案，当FeignSmsClient的timeout()方法调用
 * http接口失败时，自动调用 实现类SmsFallBackFactory里面的timeout()方法*/
@FeignClient(name = "lesson-5-sms-interface", fallbackFactory = SmsFallBackFactory.class)
@Component
interface FeignSmsClient {
	@RequestMapping(value = "/hystrix/timeout", method = RequestMethod.GET)
	public String timeout(String tempParam);// 这个参数没什么卵用，就是为了测试fallback时参数的传递

	@RequestMapping(value = "/hystrix/exception", method = RequestMethod.GET)
	public String exception();
}

// 自定义的fallback工厂，根据不同类型的异常，返回不同的降级策略
@Component
class SmsFallBackFactory implements FallbackFactory<FeignSmsClient> {
	static Logger logger = LoggerFactory.getLogger(SmsFallBackFactory.class);

	@Autowired
	FeignSmsFallbackClient feignSmsFallbackClient;

	@Override
	public FeignSmsClient create(Throwable cause) {
		// 不同情况下，异常类型是不同的
		// 连接池超限RejectedExecutionException，对应源码HystrixContextSchedulerWorker#schedule线程池任务提交类
		// hystrix命令执行超时HystrixTimeoutException,对应源码HystrixObservableTimeoutOperator超时处理类
		// 信号量模式下，并发量超限RuntimeException，对应源码AbstractCommand#handleSemaphoreRejectionViaFallback
		// 熔断开启的情况下，抛出
		logger.debug("SmsFallBackFactory收到异常：", cause);
		return feignSmsFallbackClient;
	}

}

// 错误之后的回调。 如果请求远程接口报错，hystrix会调用这些本地写的实现。 这就叫降级（既然远程接口访问不了，那就用本地的数据将就用着）。
@Component
class FeignSmsFallbackClient implements FeignSmsClient {

	@Override
	public String timeout(String tempParam) {
		System.out.println(
				"###调用timeout接口失败，导致降级,加上一个没什么卵用的参数： " + tempParam + "####" + Thread.currentThread().toString());
		return "调用timeout接口失败，导致降级";
	}

	@Override
	public String exception() {
		System.out.println("###调用有异常的接口，导致降级####" + Thread.currentThread().toString());
		return "调用有异常的接口，导致降级";
	}

}

// 降级常用的手段：
// MOCK： 返回固定的值。比如这个示例使用的就是mock值
// CACHE： 查询缓存。有些信息在通过接口无法获取时，可以查询本地的缓存。
// BACKUP： 备用接口。有些场景下，为了防止异常，会做一个备用的接口以备不时只需。
