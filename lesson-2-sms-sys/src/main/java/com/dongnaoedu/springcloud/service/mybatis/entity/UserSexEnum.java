package com.dongnaoedu.springcloud.service.mybatis.entity;

public enum UserSexEnum {
    mail(1),femail(0);
    private int flag;

    UserSexEnum(int flag) {
        this.flag = flag;
    }
}
