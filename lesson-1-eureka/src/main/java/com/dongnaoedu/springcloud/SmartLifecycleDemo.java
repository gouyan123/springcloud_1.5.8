package com.dongnaoedu.springcloud;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class SmartLifecycleDemo implements SmartLifecycle {

    private Boolean isRunning = false;
    /**
     * 返回true时start()方法会被自动执行，返回false时start()方法不会自动执行
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }
    /**
     * SmartLifecycle子类的才有的方法，当isRunning方法返回true时，该方法才会被调用。
     */
    @Override
    public void stop(Runnable callback) {
        System.out.println("=====stop");
        isRunning = false;
    }
    /**
     * 当spring上下文被刷新，所有对象已被实例化和初始化之后，将调用该方法，该方法中 可以启动任务或者其他异步服务，比如开启MQ接收消息
     */
    @Override
    public void start() {
        System.out.println("=====start");
        isRunning = true;
    }
    /**
     * 接口Lifecycle的子类的方法，只有非SmartLifecycle的子类才会执行该方法。
     * 1. 该方法只对直接实现接口Lifecycle的类才起作用，对实现SmartLifecycle接口的类无效。<br/>
     * 2. 方法stop()和方法stop(Runnable callback)的区别只在于，后者是SmartLifecycle子类的专属。
     */
    @Override
    public void stop() {
        System.out.println("=====stop");
        isRunning = false;
    }
    /**
     * 1. 只有该方法返回false时，start方法才 能被执行；
     * 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才能被执行；
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
    /**
     * 多个类 实现SmartLifecycle接口，这些类的start()方法的执行顺序按 getPhase()方法返回值从小到大 按顺序执行
     */
    @Override
    public int getPhase() {
        return 0;
    }
}
