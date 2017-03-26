package org.study.proxy.handler.impl;

import java.lang.reflect.Method;

import org.study.proxy.ProxyFactory;
import org.study.proxy.handler.InvocationHandler;
import org.study.proxy.itf.Person;
import org.study.proxy.itf.impl.Student;

public class BusinessHandler implements InvocationHandler {
	
	private Object target;
	
	public BusinessHandler(Object target) {
		this.target = target;
	}
	
	@Override
	public void call(Object handler, Method method, Object... argus) throws Exception {
		
		System.out.println("这增强类，织入了相关的增强业务逻辑");
		
		method.invoke(target, argus);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProxy() {
		return (T) ProxyFactory.getProxyInstance(target.getClass().getInterfaces()[0], this);
	}
	
	public static void main(String[] args) {
		
		Person student = new BusinessHandler(new Student()).getProxy();
		System.out.println(student);
		student.walk(10);
		student.talk("张三", "关于如何学号java");
	}
}
