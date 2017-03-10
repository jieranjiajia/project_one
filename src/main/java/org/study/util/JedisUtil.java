package org.study.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * @author ou_qu_sheng
 * jedisUtil工具类，用于把数据存入redis或者从redis读取数据，这个是直接用JedisPool来获取连接操作
 * 如果是和spring集成了，建议使用redisTemplate
 */
@Service
public class JedisUtil {

	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * Jdeis 是redis的java客户端
	 * @return
	 */
	public Jedis getResource() {
		return jedisPool.getResource();
	}
	
	/**
	 * 归还资源
	 * @param jedis
	 */
	public void returnSource(Jedis jedis) {
		Assert.notNull(jedis);
		jedisPool.returnResource(jedis);
	}
	
	/**
	 * 保存二进制的数据
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	public void saveValueByKey(byte[] key, byte[] value, int expireTime) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.set(key, value);
			if(expireTime > 0) {
				jedis.expire(key, expireTime);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			returnSource(jedis);
		}
	}
	
	public byte[] getValueByKey(byte[] key) {
		Jedis jedis = null;
		byte[] result = null;
		try {
			jedis = getResource();
			result = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnSource(jedis);
		}
		
		return result;
	}
	
	
	public <T> void saveObject(String key, T value) {
		byte[] serKey = toBytes(key);
		byte[] serValue = toBytes(value);
		saveValueByKey(serKey, serValue, -1);
	}
	
	public byte[] toBytes(Object obj) {
		if(obj instanceof String) {
			return ((String) obj).getBytes();
		} else {
			return SerializeUtil.serialize(obj);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getValueByKey(String key) {
		byte[] _key = toBytes(key);
		 byte[] valueByKey = getValueByKey(_key);
		 Object result = SerializeUtil.deserializableObject(valueByKey);
		 return (T)result;
	}
	
	public boolean deleteByKey(String key) {
		Assert.notNull(key);
		byte[] _key = toBytes(key);
		Jedis jedis = getResource();
		Long del = jedis.del(_key);
		return del > 0;
	}
}
