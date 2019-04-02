package com.dongnaoedu.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**Spring中，在xml配置文件添加task的配置，在SpringBoot中一般使用config配置类添加配置，所以新建一个AsyncConfig类*/
@Configuration
@EnableAsync
/**开启异步事件；然后在定时任务的类或者方法上添加@Async；最后重启项目，每一个任务都是在不同的线程中*/
public class AsyncConfig {
    private int corePoolSize = 5;
    private int maxPoolSize = 20;
    private int queueCapacity = 10;
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}