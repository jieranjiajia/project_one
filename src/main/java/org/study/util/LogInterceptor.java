package org.study.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用spring的aop面向切面的方法,实现了环绕增强的接口
 * 该方法主要用于计算方法的执行时间，统计方法的执行效率
 * @author ou_qu_sheng
 *
 */
public class LogInterceptor implements MethodInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

	/*@Pointcut("execution(* org.study..*.*(..))")
	public void methodPoint(){}*/
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//目标方法执行前
		String name = invocation.getMethod().getName();
		long start = System.currentTimeMillis();
		//调用目标方法，proceed是方法的返回结果
		Object proceed = invocation.proceed();
		//方法结束后
		long end = System.currentTimeMillis();
		long time = end - start;
		log.info("--------方法名{}----执行了{}毫秒",name,time);
		return proceed;
	}

	
	
}
