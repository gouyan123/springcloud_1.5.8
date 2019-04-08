package com.dongnaoedu.springcloud.rxJava;


import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Demo04 {
    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())   //被观察者 运行在这个线程，生产 事件
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .observeOn(Schedulers.newThread())   //Subscriber 即Observer 观察者 运行在这个线程，去消费事件
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        Log.d(tag, "number:" + number);
                        System.out.println(Thread.currentThread().getName() + " number:" + number);
                    }
                });
        //main线程休眠
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
