//package com.dongnaoedu.springcloud.service.mongodb.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
///**basePackages:设置Repository的子类的扫描路径
// * mongoTemplateRef:mongo模板类引用、
// * repositoryBaseClass:设置默认的Repository实现类为BaseRepositoryImpl,代替SimpleMongoRepository
// */
//@EnableMongoRepositories(basePackages = {}, mongoTemplateRef = "basicMongoTemplate")
//@ConfigurationProperties(prefix = "spring.mongodb")
//public class BasicMongoConfigure extends AbstractMongoConfigure {
//    @Override
//    @Bean(name = "basicMongoTemplate")
//    public MongoTemplate getMongoTemplate() throws Exception {
//        return new MongoTemplate(mongoDbFactory());
//    }
//}