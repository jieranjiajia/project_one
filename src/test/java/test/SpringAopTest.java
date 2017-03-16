package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.study.aop.GreetingImpl;
import org.study.aop.itf.Apology;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring.xml"})
public class SpringAopTest {
	
	public static final Logger logger = LoggerFactory.getLogger(SpringAopTest.class);

	@Autowired
	private ProxyFactoryBean factory;
	@Autowired
	private GreetingImpl greetingImpl;
	
	@Test
	public void test1() throws Exception {
		Class<?> targetClass = factory.getTargetClass();
		logger.info("targetClass--{}",targetClass);
		GreetingImpl target = (GreetingImpl)factory.getObject();
		
		logger.info("--测试的代理--{}",target);
		target.hello("manager");
		Apology apology = (Apology) target;
		apology.sorry("wang ba dan");
	}

	@Test
	public void test2() {
		greetingImpl.hello("jack");
	}
	
	@Test
	public void test3() {
		greetingImpl.sayGoodBye("admin");
	}
}
