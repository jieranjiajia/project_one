package org.study.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
		//System.out.println("动态的代理的类-----------"+proxy.toString());
		System.out.println("--jdk面向接口代理被执行----before--");
		System.out.println("--方法名是："+method.getName());
		Object object = method.invoke(target, args);
		System.out.println("--after--");
		return object;
	}

	public static void main(String[] args) {
		Greeting proxy = new JDKDynamicProxy(new GreetingImpl()).getProxy();
		proxy.hello("jack");
	}
}
