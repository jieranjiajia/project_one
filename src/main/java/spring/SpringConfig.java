package spring;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 在此处定义spring的配置文件
 * @author ou_qu_sheng
 * 这个类相当于总配置文件即spring.xml这个文件
 * Bean注解相当于在xml文件用bean标签构造一个实例,但是这种方法感觉有违spring的设计初衷解耦合，依赖关系还是蛮强的
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Configuration
public class SpringConfig {

	/**
	 * 初始化一个redisTemplate
	 *//*
	@Bean(name="redisTemplate")
	public <K,V> RedisTemplate<K, V> getRedisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(initJedisConnectionFactory());
		return redisTemplate;
	}
	
	@Bean
	public JedisPoolConfig initJedisPoolConfig() {
		return new JedisPoolConfig();
	}
	
	@Bean(autowire=Autowire.BY_NAME, name="jedisPool")
	public JedisPool getJedisPool() {
		return new JedisPool(initJedisPoolConfig(),"127.0.0.1",6379);
	}
	
	*//**
	 * 这个类是默认在localhost的6379端口写redis操作
	 *//*
	@Bean
	public JedisConnectionFactory initJedisConnectionFactory() {
		return new JedisConnectionFactory();
	}
	*/
	
}
