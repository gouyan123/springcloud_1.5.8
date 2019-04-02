package com.dongnaoedu.springcloud.rxJava;


import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class Demo01 {
    public static void main(String[] args) {
        //创建 观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                System.out.println("Item: " + s);
//            Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed!");
//            Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error!");
//            Log.d(tag, "Error!");
            }
        };
        //创建 被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override       //Subscriber就是观察者
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(observer);
    }
}
