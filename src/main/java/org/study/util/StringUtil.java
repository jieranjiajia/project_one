package org.study.util;

/**
 * 字符工具类
 *
 */
/**
 * @author hp
 *
 */
public class StringUtil {

	
	
	/** 判断一个字符串是不是为空: 不为空返回true   为空返回false
	 * @param src
	 * @return
	 */
	public static boolean isNotBlank(String src) {
		if(null == src || "".equals(src.trim())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个字符是不为空: 为空返回true  不为空返回false
	 * @param src
	 * @return
	 */
	public static boolean isBlank(String src) {
		return !isNotBlank(src);
	}
	
}
