package com.dongnaoedu.springcloud.service.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
/**basePackages:设置Repository的子接口 UserRepository的扫描目录
 * mongoTemplateRef:mongo模板类引用
 * repositoryBaseClass:设置默认的Repository实现类为BaseRepositoryImpl,代替SimpleMongoRepository
 */
@EnableMongoRepositories(basePackages = {"com.dongnaoedu.springcloud.service.mongodb.dao2"},mongoTemplateRef = "mongoTemplate2")
/**@ConfigurationProperties 使用方式有两种：
 * 1、在类上使用该注解：接收 application.yml中 spring.mongodb下面的值，存到该类的成员变量中，成员变量必须有 setter，getter方法
 * 2、在工厂方法上使用该注解，代码如下
 *     @ConfigurationProperties(prefix = "spring.datasource.sakila")
 *     public DataSource dataSource() {
 *         return DataSourceBuilder.create().build();
 *     }
 * */
@ConfigurationProperties(prefix = "spring.mongodb2")
public class MongoConfig2 {
    String host;
    int port;
    String username;
    String password;
    String database;

    @Bean
    @RefreshScope
    public MongoDbFactory mongoDbFactory(){
        ServerAddress serverAddress = new ServerAddress(host,port);
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
        mongoCredentialList.add(MongoCredential.createCredential(username,database,password.toCharArray()));
        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList),database);
    }

    @Bean(name = "mongoTemplate2")
    @RefreshScope
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoDbFactory());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}