package org.study.proxy.handler;

import java.lang.reflect.Method;

/**
 * 面向接口动态代理
 * 定义一个进行代理逻辑或业务的接口，
 * 具体的代理业务逻辑：比如做计算方法运行时间，拦截日志，事务处理等等由具体实现该接口的具体类来实现
 * 计算方法的：CalMethodRuntimeHandler implements InvocationHandler ...
 * 日志处理的：LoggerHandler implements InvocationHandler ...
 * 进行具体的代理操作。比如做增强处理，做日志处理等等。（首先要实现该接口）
 */
public interface InvocationHandler {
	
	/**
	 * 
	 * @param handler 为实现了InvocationHandler接口的实现类
	 * @param method  为代理目标接口的方法
	 * @param argus   为method的入参
	 * @throws Exception
	 */
	void call(Object handler, Method method, Object... argus) throws Exception;
	
}
