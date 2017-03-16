package org.study.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Random;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;
/**
 * spring集成了aop团队的方法拦截器，相当于Around的环绕增强
 * 但是这种方法还是不够灵活。
 * 第一点：这个需要spring的ProxyFactoryBean（在spring的配置文件中使用）或 ProxyFactory（在java代码中使用）
 * 来添加被代理的类（目标类）--比如***Impl
 * 第二点：需要添加增强类 比如实现了MethodInterceptor的环绕增强会对目标类中所有的方法进行增强处理
 *       这种方法并不是很灵活
 * 第三点：ProxyFactoryBean 中配置 proxyTargetClass 来定义使用那种动态代理，一旦配置就无法灵活的调用与切换
 * 
 * 但总体来说比直接在java中写死进步很多，后面还有更好的更灵活的代理实施方案(spring+aspectj 或者 spring+aop的配置)
 */
@Component
public class GreetingAdvice implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		System.out.println("--before--");
		long start = System.currentTimeMillis();
		Method method = methodInvocation.getMethod();
		Annotation[] annotations = method.getAnnotations();
		System.out.println("annotations size " + annotations.length);
		Object proceed = methodInvocation.proceed();
		Thread.sleep(new Random().nextInt(1000));
		System.out.println("--after--");
		long end = System.currentTimeMillis();
		System.out.println("测试耗时："+ (end-start));
		return proceed;
	}

	
	
	public static void main(String[] args) {
		ProxyFactory factory = new ProxyFactory();
		factory.addAdvice(new GreetingAdvice());             //添环绕增强
		factory.setTarget(new GreetingImpl());				 //添加目标
		
		GreetingImpl proxy = (GreetingImpl)factory.getProxy();
		proxy.hello("admin");
		
	}	
}
