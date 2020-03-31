package com.nian.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * �0�0�1�7�1�7@EnableTransactionManagement�1�7�1�7�1�7�1�7�1�7�1�7�0�6�1�7�1�7TransactionManagementConfigurer�1�7�1�7
 * �1�7�1�7�0�2�1�7�1�7�0�6�1�7�1�7�0�0�1�7�1�7�0�4�1�7�1�7�1�7�1�7�1�7�2�5�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�0�1�7�1�7@Transactional�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
 * annotationDrivenTransactionManager�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�5�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
 * 
 * �1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�7�0�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�6SqlSessionFactoryBean�1�7�1�7MyBatis�1�7�1�7�0�9�1�7�1�7�1�7�1�8�1�7�1�7�1�7�1�7MyBatis�1�7�1�7�0�9�1�7�1�7�1�7�1�7�0�0�1�7�1�7
 * �0�0�1�7�1�7@Repository�1�7�0�8�1�7�1�7�1�7("com.*")�1�7�1�8�1�7�1�7�1�7�1�7�1�7�1�7�1�7MyBatis�1�7�0�9�1�7�0�0�1�7�1�7Spring�1�7�0�3�1�7�1�7�1�7�1�7�0�9�1�7�1�7�1�7�0�8�1�7�0�5�0�3�1�6�1�7�1�7�1�7�1�7�0�0�1�7
 * Spring�1�7�1�7�0�6�1�7�0�8�1�7�0�5�0�3�1�7�0�4�1�7�5�3IoC�1�7�1�7�1�7�1�7�1�7���1�7
 * 
 * @author Niantianlei
 */
@Configuration
//�1�7�1�7�1�7�1�7Spring �0�9�1�7�1�7�0�2�1�7
@ComponentScan(value= "com.*", includeFilters= {@Filter(type = FilterType.ANNOTATION, value ={Service.class})})
//�0�0�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
@EnableTransactionManagement
//�0�6�1�7�0�3�0�3�1�7TransactionManagementConfigurer�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�0�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
public class RootConfig implements TransactionManagementConfigurer {
	
	private DataSource dataSource = null;
	
	/**
	 * �1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�3�1�7
	 * @return �1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�1�1�7
	 */
	@Bean(name = "dataSource")
	public DataSource initDataSource() {
		if (dataSource != null) {
			return dataSource;
		}
		Properties props = new Properties();
		props.setProperty("driverClassName", "com.mysql.jdbc.Driver");
		props.setProperty("url", "jdbc:mysql://localhost:3306/chapter22");
		props.setProperty("username", "root");
		props.setProperty("password", "admin");
        props.setProperty("maxActive", "200");
		props.setProperty("maxIdle", "20");
		props.setProperty("maxWait", "30000");
		try {
			dataSource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}
	
	/***
	 * �1�7�1�7�1�7�1�7SqlSessionFactoryBean
	 * @return SqlSessionFactoryBean
	 */
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactory() {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(initDataSource());
		//�1�7�1�7�1�7�1�7MyBatis�1�7�1�7�1�7�1�7�1�7�0�4�1�7
		Resource resource = new ClassPathResource("mybatis-config.xml");
		sqlSessionFactory.setConfigLocation(resource);
		return sqlSessionFactory;
	}
	
	/***
	 * �0�0�1�7�1�7�1�7�0�8�1�7�0�9�1�7�k�1�7�1�7�1�7�1�7MyBatis Mapper�1�7�0�3�1�7
	 * @return Mapper�0�9�1�7�1�7�1�7�1�7
	 */
	@Bean 
	public MapperScannerConfigurer initMapperScannerConfigurer() {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.*");
		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		msc.setAnnotationClass(Repository.class);
		return msc;
	}
	
	
	/**
	 * �0�6�1�7�0�3�0�3�1�3�1�7�1�7�1�7�1�7�1�7�0�0�1�7�1�7�0�0�1�7�1�7�1�7�1�7�1�7�2�7�1�7@Transactional �0�0�1�7�0�1�1�7�0�2�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�3�1�7�1�7�1�7�1�7�1�7 
	 */
	@Override
	@Bean(name="annotationDrivenTransactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		DataSourceTransactionManager transactionManager = 
           new DataSourceTransactionManager();
		transactionManager.setDataSource(initDataSource());
		return transactionManager;
	}
	
	/*
	 * �1�7�1�7�1�7�1�7�0�5�1�7�1�7RedisTemplate�1�7�1�7�1�7�8�2�1�7�0�4�1�7�1�3�1�7IoC�1�7�1�7�1�7�1�7�1�7���1�7
	 * �1�7�1�7�1�7�1�7RedisTemplate�1�7�1�7�1�7�1�7�1�7�1�7Spring�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�0�1�7�1�7�1�7�0�5�1�7
	 * JedisConnectionFactory�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�2�1�7�1�7�1�7�1�7�0�8�1�7�1�7�1�7�ք1�7�1�7�1�7afterPropertiesSet�1�7�1�7�1�7�1�7�1�7�1�7
	 * �1�7�1�7�0�6�1�7�1�7�1�7�1�7InitializingBean�1�7�0�3�1�1�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7IoC�1�7�1�7�1�7�1�7�1�7���1�7Spring�1�7�1�7�1�7�0�8�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�Մ1�7�1�7�1�7�1�7�1�7
	 * �1�7�1�7�0�2�1�7�1�7�0�8�1�7�1�7�1�7�ք1�7�1�7�0�0�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�1�1�7�0�2�1�7�1�7�1�7�1�7�0�7�1�7�1�7�4�4�1�7�1�7�1�7�1�7�1�7�0�4�1�7�1�7�1�7
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate initRedisTemplate() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
		poolConfig.setMaxIdle(50);
		//�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7
		poolConfig.setMaxTotal(100);
		//�1�7�1�7�1�7�0�9�1�7�1�7�1�7�1�7�1�7�1�7�1�7
		poolConfig.setMaxWaitMillis(20000);
		//�1�7�1�7�1�7�1�7Jedis�1�7�1�7�1�7�0�7�1�7�1�7�1�7
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);
		//�1�7�1�7�1�7���1�7�1�7�0�3�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�4�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�7�1�7�1�7�4�4
		connectionFactory.afterPropertiesSet();
		//�1�7�0�8�1�7Redis�1�7�1�7�1�7�݄1�7�1�7�1�7
		RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		RedisSerializer stringRedisSerializer = new StringRedisSerializer();
		//�1�7�1�7�1�7�1�7RedisTemplate�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�1�7�0�7�1�7�1�7�1�7[�1�7�1�0�1�7�0�2�1�7�1�7�1�7�1�7�1�7�1�7]
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		//�1�7�1�7�1�7�1�7�1�7�1�7�1�7�݄1�7�1�7�1�7
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(stringRedisSerializer);
		return redisTemplate;
	}
	
}