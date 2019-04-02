//package com.dongnaoedu.springcloud.service.mybatis.controller;
//
//import com.dongnaoedu.springcloud.service.mybatis.dao1.UserMapper1;
//import com.dongnaoedu.springcloud.service.mybatis.dao2.UserMapper2;
//import com.dongnaoedu.springcloud.service.mybatis.entity.UserEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/user")
//public class UserController {
//    @Autowired
//    private UserMapper1 userMapper1;
//    @Autowired
//    private UserMapper2 userMapper2;
//
//    /*get请求 http://localhost:9002/user/save1?userName=t_1&passWord=qwe*/
//    @RequestMapping(value = "/save1",method = RequestMethod.GET)
//    public String save1(String userName,String passWord){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName(userName);
//        userEntity.setPassWord(passWord);
//        userMapper1.insert(userEntity);
//        return "success";
//    }
//    /*get请求 http://localhost:9002/user/save2?userName=t_2&passWord=qwe*/
//    @RequestMapping(value = "/save2",method = RequestMethod.GET)
//    public String save2(String userName,String passWord){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName(userName);
//        userEntity.setPassWord(passWord);
//        userMapper2.insert(userEntity);
//        return "success";
//    }
//}
