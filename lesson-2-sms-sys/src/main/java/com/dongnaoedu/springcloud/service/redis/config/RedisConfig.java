package com.dongnaoedu.springcloud.service.redis.config;

import com.dongnaoedu.springcloud.service.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration

public class RedisConfig {
    @Autowired
    Environment env;

    @Bean(name = "jedisPoolConfig")
    @RefreshScope
    @ConfigurationProperties("spring.redis.pool")
    public JedisPoolConfig jedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        /*jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);*/
        return jedisPoolConfig;
    }
    /**单机版配置*/
    @Bean(name = "jedisConnectionFactory1")
    @RefreshScope
    @Primary
    public JedisConnectionFactory JedisConnectionFactory(@Qualifier("jedisPoolConfig1") JedisPoolConfig jedisPoolConfig){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig);
        return factory;
    }

    /**实例化 RedisTemplate 对象*/
    @Bean(name = "redisTemplate1")
    @RefreshScope
    public RedisTemplate<String, Object> functionDomainRedisTemplate(@Qualifier("jedisConnectionFactory1") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }
    /**设置数据存入 redis 的序列化方式,并开启事务*/
    private void initDomainRedisTemplate(@Qualifier("redisTemplate1") RedisTemplate<String, Object> redisTemplate,
                                         @Qualifier("JedisConnectionFactory1") RedisConnectionFactory factory) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
    }
    /**注入封装RedisTemplate*/
    @Bean(name = "redisUtil1")
    @RefreshScope
    public RedisUtil redisUtil(@Qualifier("redisTemplate1")RedisTemplate<String, Object> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
}