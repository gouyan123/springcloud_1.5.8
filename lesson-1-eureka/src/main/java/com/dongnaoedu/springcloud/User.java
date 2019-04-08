package com.dongnaoedu.springcloud;

public class User {
    public static void main(String[] args) {
        User user = new Builder().name("James").age(34).build();
        System.out.println(user.getName() + " " + user.getAge());
    }
    private String name;
    private Integer age;

    public User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    static class Builder{
        private String name;
        private Integer age;

        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder age(Integer age){
            this.age = age;
            return this;
        }
        public User build(){
            return new User(this);
        }
    }
}

