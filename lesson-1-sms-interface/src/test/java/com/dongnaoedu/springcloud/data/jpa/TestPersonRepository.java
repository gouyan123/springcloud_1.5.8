package com.dongnaoedu.springcloud.data.jpa;

import com.dongnaoedu.springcloud.SmsInterfaceApp;
import com.dongnaoedu.springcloud.data.jpa.dao.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = SmsInterfaceApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPersonRepository {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void test01(){
        long count = personRepository.count();
        System.out.println(count);
    }


}
