package org.study.aop;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
/**
 * 使用CGLib实施动态代理
 * 这是相当于使用CGLib对一个类做增强处理，实现spring框架下的MethodInterceptor
 * CGLib代理可以对类做代理，而jdk动态代理是面向接口的代理
 * 这个cjlib代理不能被ProxyFactoryBean识别
 */
@Component(value="cGLibDynamicProxy")
public class CGLibDynamicProxy implements MethodInterceptor {
	
	private static final CGLibDynamicProxy instance = new CGLibDynamicProxy();
	
	/**
	 * 构造方法私有
	 */
	private CGLibDynamicProxy() {
		
	}

	public static CGLibDynamicProxy instance() {
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> cls) {
		return (T)Enhancer.create(cls, this);
	}
	
	@Override
	public Object intercept(Object paramObject, Method paramMethod, Object[] paramArrayOfObject,
			MethodProxy paramMethodProxy) throws Throwable {
		System.out.println("--cjlig代理----before--");
		System.out.println("方法名--" + paramMethod.getName());
		System.out.println("代理的方法名--："+ paramMethodProxy.getSignature().getName());
		
		/* 
		 * 调用paramMethod时会出现递归的操作
		 */
//		Object invoke = paramMethod.invoke(paramObject, paramArrayOfObject);
		
		Object invoke = paramMethodProxy.invokeSuper(paramObject, paramArrayOfObject);
		
		System.out.println("--after--");
		return invoke;
	}

	public static void main(String[] args) {
		GreetingImpl proxy = CGLibDynamicProxy.instance().getProxy(GreetingImpl.class);
		proxy.hello("sam");
		
	}
	
	
	
}
