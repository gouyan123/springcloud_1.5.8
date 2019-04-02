package com.dongnaoedu.springcloud.data.jpa.controller;

import com.dongnaoedu.springcloud.data.jpa.domain.Person;
import com.dongnaoedu.springcloud.data.jpa.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;
    /*http://localhost:9002/person/save?name=James&address=USA&age=33*/
    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public Person save(String name, String address, Integer age, HttpServletRequest request){

        Person person = new Person();
            person.setName(name);
            person.setAddress(address);
            person.setAge(age);
            person = personRepository.save(person);
        return person;
    }

    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Page<Person> findAll(int pageNum,int pageSize){
        Page<Person> persons = personRepository.findAll(new PageRequest(pageNum,pageSize));
        return persons;
    }
    /**------------------------------------------------------------------------------------------*/
    @RequestMapping(value = "/findByNameEqualsAndAddressEquals",method = RequestMethod.GET)
    public List<Person> findByNameEqualsAndAddressEquals(String name,String address){
        List<Person> persons = personRepository.findByNameEqualsAndAddressEquals(name,address);
        return persons;
    }

    @RequestMapping(value = "/findByNameContaining",method = RequestMethod.GET)
    public List<Person> findByNameContaining(String name){
        List<Person> persons = personRepository.findByNameContaining("J");
        return persons;
    }

    @RequestMapping(value = "/findByAgeAfter",method = RequestMethod.GET)
    public List<Person> findByAgeAfter(int age){
        List<Person> persons = personRepository.findByAgeAfter(age);
        return persons;
    }

    @RequestMapping(value = "/searchByName",method = RequestMethod.GET)
    public List<Person> searchByName(String name){
        List<Person> persons = personRepository.searchByName(name);
        return persons;
    }

    @RequestMapping(value = "/updateAgeByName",method = RequestMethod.GET)
    public int updateAgeByName(String name,int age){
        int count = personRepository.updateAgeByName(name,age);
        return count;
    }

    @RequestMapping(value = "/searchJoin",method = RequestMethod.GET)
    public List<Object[]> searchJoin(){
        List<Object[]> objs= personRepository.searchJoin();
        return objs;
    }
    /*http://localhost:9002/person/nativeQuery?age=23*/
    @RequestMapping(value = "/nativeQuery",method = RequestMethod.GET)
    public List<Person> nativeQuery(int age){
        List<Person> persons= personRepository.nativeQuery(age);
        return persons;
    }
    /**分页查询，page表示当前页数，size表示每页数据条数*/
    /*http://localhost:9002/person/page?page=1&size=2*/
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Page<Person> page(int page,int size){
        /**创建 排序对象sort，封装 排序类型，排序字段*/
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        /**创建 分页对象pageable，封装 当前所在页，每页数据条数，排序对象*/
        Pageable pageable = new PageRequest(page, size, sort);
        /**将分页对象pageable传入查询方法，返回Page对象*/
        Page<Person> pages= personRepository.findAll(pageable);
        return pages;
    }
}
