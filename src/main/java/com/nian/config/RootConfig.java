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
 * ע��@EnableTransactionManagement������ʵ��TransactionManagementConfigurer��
 * ��Ϊ��ʵ��ע��ʽ�����񣬽�������ͨ��@Transactional��������
 * annotationDrivenTransactionManager��������һ�������������
 * 
 * ���������⣬������������ԴSqlSessionFactoryBean��MyBatis��ɨ���࣬����MyBatis��ɨ����ͨ��
 * ע��@Repository�Ͱ���("com.*")�޶�������MyBatis�ͻ�ͨ��Spring�Ļ����ҵ���Ӧ�Ľӿں����ã�
 * Spring��Ѷ�Ӧ�Ľӿ�װ�䵽IoC�����С�
 * 
 * @author Niantianlei
 */
@Configuration
//����Spring ɨ��İ�
@ComponentScan(value= "com.*", includeFilters= {@Filter(type = FilterType.ANNOTATION, value ={Service.class})})
//ʹ����������������
@EnableTransactionManagement
//ʵ�ֽӿ�TransactionManagementConfigurer��������������ע����������
public class RootConfig implements TransactionManagementConfigurer {
	
	private DataSource dataSource = null;
	
	/**
	 * �������ݿ�
	 * @return �������ӳ�
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
	 * ����SqlSessionFactoryBean
	 * @return SqlSessionFactoryBean
	 */
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactory() {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(initDataSource());
		//����MyBatis�����ļ�
		Resource resource = new ClassPathResource("mybatis-config.xml");
		sqlSessionFactory.setConfigLocation(resource);
		return sqlSessionFactory;
	}
	
	/***
	 * ͨ���Զ�ɨ�裬����MyBatis Mapper�ӿ�
	 * @return Mapperɨ����
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
	 * ʵ�ֽӿڷ�����ע��ע�����񣬵�@Transactional ʹ�õ�ʱ��������ݿ����� 
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
	 * ����һ��RedisTemplate���󣬲�װ�ص�IoC�����С�
	 * ����RedisTemplate������Spring��������ʹ���ˡ�
	 * JedisConnectionFactory����������ʱ����Ҫ���е���afterPropertiesSet������
	 * ��ʵ����InitializingBean�ӿڡ��������������IoC�����У�Spring���Զ������������������������д�����
	 * ��Ϊ��Ҫ���е��ã����������õ�ʱ����׳��쳣�����ִ���
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate initRedisTemplate() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//��������
		poolConfig.setMaxIdle(50);
		//���������
		poolConfig.setMaxTotal(100);
		//���ȴ�������
		poolConfig.setMaxWaitMillis(20000);
		//����Jedis���ӹ���
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);
		//���ú��ʼ��������û�������׳��쳣
		connectionFactory.afterPropertiesSet();
		//�Զ�Redis���л���
		RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		RedisSerializer stringRedisSerializer = new StringRedisSerializer();
		//����RedisTemplate�����������ӹ���[�޸�Ϊ������]
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		//�������л���
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(stringRedisSerializer);
		return redisTemplate;
	}
	
}