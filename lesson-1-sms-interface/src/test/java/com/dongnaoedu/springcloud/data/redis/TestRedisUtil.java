package com.dongnaoedu.springcloud.data.redis;

import com.alibaba.fastjson.JSON;
import com.dongnaoedu.springcloud.SmsInterfaceApp;
import com.dongnaoedu.springcloud.data.jpa.dao.PersonRepository;
import com.dongnaoedu.springcloud.data.redis.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = SmsInterfaceApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRedisUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testSetGet(){
        redisUtil.set("MVP","James");
        Object result = redisUtil.get("MVP");
        System.out.println(JSON.toJSONString(result,true));
    }
}
