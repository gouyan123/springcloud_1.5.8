package com.dongnaoedu.springcloud.data.jpa.domain;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class BaseEntity implements Serializable {
    /*当前页码，默认为1*/
    private int page = 1;
    /*每页显示页数，默认为20*/
    private int size;
    /*排序列 字段名称，默认为 id*/
    private String sidx = "id";
}
