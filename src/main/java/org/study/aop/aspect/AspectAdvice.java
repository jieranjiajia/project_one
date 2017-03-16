package org.study.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 利用Aspect注解使用Aspect织入
 * 使用这个方法之后只需要在spring的配置文件中扫描这个类
 * 
 *  
 */
@Aspect
@Component
public class AspectAdvice {
	/*
	 * @DeclareParents 引入代理相当于spring的IntroductionAdvice
	 * @Before         
	 * @After
	 * @AfterThrowing
	 * @Around
	 */
	private static final Logger log = LoggerFactory.getLogger(AspectAdvice.class);
	
	/**
	 * 定义一个切点 
	 * 第一个*表示任意返回值
	 * 第二个*表示GreetingImpl类中的任意方法
	 * ..表示任意的参数 
	 */
	@Pointcut("execution( public * org.study.aop.GreetingImpl.*(..))")
	public void point(){}
	
	/**
	 * 定义一个注解类型的切点，只要是被该注解标注的方法就会被拦截，进行相应的增强处理
	 */
	@Pointcut("@annotation(org.study.aop.aspect.annotation.CalMethodTime)")
	public void annotationPointcut() {}

	/**
	 * 定义一个环绕增强处理的方法
	 * 必须引入ProceedingJoinPoint 这个参数，从这个参数中可以获取调用的方法参数
	 * 使用 point()方法定义的切点  
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	@Around(value="point()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		
		log.info("AspectAdvice类  around 方法：开始执行");
		long start = getCurrentTime();
		//获取方法的签名
		Signature signature = point.getSignature();
		
		log.info("拦截到的方法签名是-->{}",signature.getName());
		String declaringTypeName = signature.getDeclaringTypeName();
		log.info("declaringTypeName是-->{}",declaringTypeName);
		
		Object[] args = point.getArgs();
		
		log.info("方法的参数是-->{}",args);
		
		Object this1 = point.getThis();
		
		log.info("当前的目标对象是-->{}",this1);
		
		Thread.sleep(1000);
		//执行目标方法
		Object proceed = point.proceed();
		log.info("返回值是-->{}",proceed);
		
		long end = getCurrentTime();
		
		log.info("方法功经历的时间-->{}(毫秒)",(end - start));
		return proceed;
	}
	
	@Around("annotationPointcut()")
	public void annotationAround(ProceedingJoinPoint point) throws Throwable {
		long start = getCurrentTime();
		point.proceed();
		long end = getCurrentTime();
		
		log.info("-------被注解的方法耗时{}",(end - start));
	}
	
	
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
