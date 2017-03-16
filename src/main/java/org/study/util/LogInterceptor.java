package org.study.util;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * spring集成了aop团队的方法拦截器，相当于Around的环绕增强
 * 
 *
 */
@Component
public class LogInterceptor implements MethodInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		
		Object[] arguments = arg0.getArguments();
		Method method = arg0.getMethod();
		log.info("日志：方法的参数>>{}",arguments);
		log.info("日志：方法签名>>{}",method.getName());
		Object proceed = arg0.proceed();
		log.info("日志：方法的返回值>>{}" ,proceed );
		return proceed;
	}


	
	
}
