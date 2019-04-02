package com.dongnaoedu.springcloud.data.jpa.dao;

import com.dongnaoedu.springcloud.data.jpa.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
/*-------------------------------------------------------------------------------------------------------*/
    /*使用命名规则查询*/
    /**使用命名规则 查询：查询 name=参数 and address=参数，返回列表*/
    List<Person> findByNameEqualsAndAddressEquals(String name,String address);
    /**使用命名规则 查询：查询 name like %参数%，返回列表*/
    List<Person> findByNameContaining(String name);
    /**使用命名规则 查询：查询 age > 参数，返回列表*/
    List<Person> findByAgeAfter(int age);

/*-------------------------------------------------------------------------------------------------------*/
    /*使用@Query查询*/
    /**使用 HQL语句 查询*/
    @Query("select p from Person p where p.name = :name")
    List<Person> searchByName(@Param("name") String name);

    /**使用 HQL语句 更新*/
    @Modifying
    @Transactional(readOnly = false)    /**@Modifying表示更新，@Transactional表示修改数据表时开启事务；*/
    @Query("update Person p set p.age=:age where name=:name")
    int updateAgeByName(@Param("name") String name,@Param("age") int age);

    /**使用 原生sql 查询，nativeQuery=true表示使用原生查询*/
    @Query(nativeQuery = true,value = "select * from t_person where age > ?1")
    public List<Person> nativeQuery(int age);

    /**使用 原生sql 关联查询*/
    @Query(nativeQuery = true,value = "select p.name,t.team_name from t_person p left join t_team t on p.team_id=t.id")
    List<Object[]> searchJoin();
}
