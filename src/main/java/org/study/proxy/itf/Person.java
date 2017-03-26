package org.study.proxy.itf;
/**
 * jdk的动态代理，是面向接口式的代理
 * 定一个Person的抽象接口。
 */
public interface Person {
	/**
	 * 名叫那么的人，说了speek的内容
	 * @param str
	 */
	void talk(String name, String speek);
	
	/**
	 * 走了miles里路
	 * @param miles
	 */
	void walk(int miles);
	
}
