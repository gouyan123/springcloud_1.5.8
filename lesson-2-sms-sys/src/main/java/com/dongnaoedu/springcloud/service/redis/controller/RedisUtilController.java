package com.dongnaoedu.springcloud.service.redis.controller;

import com.dongnaoedu.springcloud.service.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisUtilController {
    @Autowired
    @Qualifier(value = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate1;

    @Autowired
    @Qualifier(value = "redisTemplate2")
    private RedisTemplate<String,Object> redisTemplate2;

    /* get请求 http://localhost:9002/redis/save1?key=gy1&value=123*/
    @RequestMapping(value = "/save1",method = RequestMethod.GET)
    public String save1(String key,String value){
        redisTemplate1.opsForValue().set(key,value);
        return "success";
    }
    /*get请求 http://localhost:9002/redis/save2?key=gy2&value=123*/
    @RequestMapping(value = "/save2",method = RequestMethod.GET)
    public String save2(String key,String value){
        redisTemplate2.opsForValue().set(key,value);
        return "success";
    }
}
