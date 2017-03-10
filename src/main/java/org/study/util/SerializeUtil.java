package org.study.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.util.Assert;

/**
 * 把一个对象进行序列化和反序列化
 * @author ou_qu_sheng
 *
 */
public class SerializeUtil {

	public static byte[] serialize(Object value) {
		byte[] arr = null;
		Assert.notNull(value);
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);) {
			oos.writeObject(value);
			arr = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	
	public static Object deserializableObject(byte[] in) {
		return deserialize(in, Object.class);
	}
	
	
	@SuppressWarnings({"unchecked"})
	public static <T> T deserialize(byte[] in, Class<T> requiredType) {
		Object entity = null;
		Assert.notNull(in);
		try(ByteArrayInputStream bis = new ByteArrayInputStream(in);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			entity = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)entity; 
	}
	
		
}
