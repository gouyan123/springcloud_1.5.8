package com.dongnaoedu.springcloud;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(3);
        long initialDelay = 1;
        long period = 1;

        // 从现在开始1秒钟之后，每隔1秒钟执行一次job1
        Runnable r = () -> {
            System.out.println("world" + System.currentTimeMillis());
        };
//        schedule.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.SECONDS);
//        schedule.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("hello");
//            }
//        },initialDelay,TimeUnit.SECONDS);
        schedule.schedule(new MyTimerTask(),initialDelay,TimeUnit.SECONDS);


        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyTimerTask extends TimerTask{

    @Override
    public void run() {
        System.out.println("hello");
    }
}