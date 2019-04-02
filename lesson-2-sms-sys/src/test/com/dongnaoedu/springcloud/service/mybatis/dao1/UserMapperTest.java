//package com.dongnaoedu.springcloud.service.mybatis.dao1;
//
//import com.dongnaoedu.springcloud.service.SmsServiceApplication;
//import com.dongnaoedu.springcloud.service.mybatis.dao1.UserMapper;
//import com.dongnaoedu.springcloud.service.mybatis.entity.UserEntity;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = SmsServiceApplication.class)
//public class UserMapperTest {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    public void testInsert() throws Exception {
//        userMapper.insert(new UserEntity("aa", "a123456"/*, UserSexEnum.MAN*/));
//        userMapper.insert(new UserEntity("bb", "b123456"/*, UserSexEnum.WOMAN*/));
//        userMapper.insert(new UserEntity("cc", "b123456"/*, UserSexEnum.WOMAN*/));
//    }
//
//    @Test
//    public void testQuery() throws Exception {
//        List<UserEntity> users = userMapper.getAll();
//        System.out.println(users.toString());
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        UserEntity user = userMapper.getOne(3l);
//        System.out.println(user.toString());
//        user.setNickName("neo");
//        userMapper.update(user);
//        Assert.assertTrue(("neo".equals(userMapper.getOne(3l).getNickName())));
//    }
//}