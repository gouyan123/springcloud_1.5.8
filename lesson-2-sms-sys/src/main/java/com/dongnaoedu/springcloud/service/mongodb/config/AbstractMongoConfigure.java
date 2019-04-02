//package com.dongnaoedu.springcloud.service.mongodb.config;
//
//import com.mongodb.MongoClient;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//
//public abstract class AbstractMongoConfigure {
//    private String host;
//    private String database;
//    private int port;
//
//    public MongoDbFactory mongoDbFactory() throws Exception {
//        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
//    }
//
//    /*Factory method to create the MongoTemplate*/
//    abstract public MongoTemplate getMongoTemplate() throws Exception;
//
//    public String getHost() {
//        return host;
//    }
//    public void setHost(String host) {
//        this.host = host;
//    }
//    public String getDatabase() {
//        return database;
//    }
//    public void setDatabase(String database) {
//        this.database = database;
//    }
//    public int getPort() {
//        return port;
//    }
//    public void setPort(int port) {
//        this.port = port;
//    }
//}