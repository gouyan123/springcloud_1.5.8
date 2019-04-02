package com.tony.springcloud;

public class Father {
    private int age;
    protected String name;
    public Father() {
        System.out.println("无参构造");
    }

    public Father(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("有参构造");
    }

    public void setName(String name) {
        this.name = name;
    }
    private void setAge(int age){
        this.age = age;
    }
}

class Son extends Father{
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public static void main(String[] args) {
        Son son = new Son();
        son.setName("James");

        System.out.println(son.name);
    }
}