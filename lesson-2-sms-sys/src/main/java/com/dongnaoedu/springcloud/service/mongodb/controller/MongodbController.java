package com.dongnaoedu.springcloud.service.mongodb.controller;

import com.dongnaoedu.springcloud.service.mongodb.dao1.UserRepository1;
import com.dongnaoedu.springcloud.service.mongodb.dao2.UserRepository2;
import com.dongnaoedu.springcloud.service.mongodb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mongodb")
public class MongodbController {

    @Autowired
    private UserRepository1 userRepository1;
    /*http://127.0.0.1:9002/mongodb/save1?name=James&age=34*/
    @RequestMapping(value = "/save1",method = RequestMethod.GET)
    public String save(String name,int age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        userRepository1.save(user);
        return "mongodb success";
    }

    @Autowired
    private UserRepository2 userRepository2;
    /*http://127.0.0.1:9002/mongodb/save2?name=James&age=34*/
    @RequestMapping(value = "/save2",method = RequestMethod.GET)
    public String save2(String name,int age){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        userRepository2.save(user);
        return "mongodb success";
    }
}
