package org.study.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

import org.study.aop.itf.Greeting;
/**
 * 使用jdk的动态代理
 * @author ou_qu_sheng
 *
 */
public class JDKDynamicProxy implements InvocationHandler {
	
	/**
	 * 被代理的对象
	 */
	private Object target;
	
	
	public JDKDynamicProxy(Object target) {
		this.target = target;
	}
	
	@SuppressWarnings({"unchecked"})
	public <T> T getProxy() {
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("--jdk面向接口代理被执行----before--");
		long start = getCurrentTime();
		Object object = method.invoke(target, args);
		Thread.sleep(new Random().nextInt(10000));
		long end = getCurrentTime();
		System.out.println("--after--");
		System.out.println(method.getName() +"耗时----："+(end-start));
		return object;
	}

	
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	public static void main(String[] args) {
		Greeting proxy = new JDKDynamicProxy(new GreetingImpl()).getProxy();
		proxy.hello("jack");
	}
}
