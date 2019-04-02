package com.dongnaoedu.springcloud.rxJava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Demo06 {
    public static void main(String[] args) {
        Student[] students = {new Student("James"),new Student("Jordan")};
        //创建观察者，<String> 为事件
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error");
            }

            @Override
            public void onNext(String name) {
                System.out.println(name);
//                Log.d(tag, name);
            }
        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);
    }
}
class Student{
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
