package com.dongnaoedu.springcloud.rxJava;

import rx.Observable;
import rx.functions.Action1;

public class Demo03 {
    public static void main(String[] args) {
        String[] names = {"James","Jordan","Kobe"};
        //被监听者 里面注册 监听者
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
//                        Log.d(tag, name);
                        System.out.println(name);
                    }
                });
    }
}
