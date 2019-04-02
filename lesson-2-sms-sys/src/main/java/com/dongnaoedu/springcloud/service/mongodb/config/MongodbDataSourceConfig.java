//package com.dongnaoedu.springcloud.service.mongodb.config;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
///**接收 application.yml中 spring.mongodb下面的属性的值，存到该类的成员变量中*/
//@ConfigurationProperties(prefix = "spring.mongodb")
//public class MongodbDataSourceConfig {
//    private String host;
//    private int port;
//    private String database;
//    private String username;
//    private String password;
//
//    @Bean
//    @RefreshScope
//    public MongoDbFactory mongoDbFactory(){
//        ServerAddress serverAddress = new ServerAddress(host,port);
//        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
//        mongoCredentialList.add(MongoCredential.createCredential(username,database,password.toCharArray()));
//        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList),database);
//    }
//
//    @Bean
//    @RefreshScope
//    public MongoTemplate mongoTemplate(){
//        return new MongoTemplate(mongoDbFactory());
//    }
//}
