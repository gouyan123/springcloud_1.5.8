//package com.dongnaoedu.springcloud.service.mybatis.config;
//
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import javax.sql.DataSource;
//
//@Configuration
///**basePackages定义扫描哪个dao，并指定给dao层注入的 sqlSessionTemplateRef*/
//@MapperScan(basePackages = {"com.dongnaoedu.springcloud.service.mybatis.dao2"}, sqlSessionTemplateRef  = "sqlSessionTemplate2")
//public class MybatisConfig2 {
//    @Autowired
//    Environment env;
//
//    @Primary
//    @Bean(name = "dataSource2")
//    /*@ConfigurationProperties(prefix = "jdbc")*/
//    /**@RefreshScope 将该方法定义为刷新范围，当refresh刷新配置时，JedisPool这个实例会被刷新*/
//    @RefreshScope
//    public DruidDataSource dataSource() {
//        /*DruidDataSource druidDataSource = (DruidDataSource) DataSourceBuilder.create().build();*/
//
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(env.getProperty("jdbc2.driver"));
//        String baseUrl = env.getProperty("jdbc2.baseurl");
//        String subUrl = env.getProperty("jdbc2.suburl");
//        dataSource.setUrl(baseUrl + subUrl);
//        /*dataSource.setUrl(env.getProperty("jdbc.url"));*/
//        dataSource.setUsername(env.getProperty("jdbc2.username"));
//        dataSource.setPassword(env.getProperty("jdbc2.password"));
//        return dataSource;
//    }
//
//    @Bean(name = "sqlSessionFactory2")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        //添加XML目录
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        try {
//            bean.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper2/*.xml"));
//            return bean.getObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Bean(name = "sqlSessionTemplate2")
//    @RefreshScope
//    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) throws Exception {
//        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
//        return template;
//    }
//}
