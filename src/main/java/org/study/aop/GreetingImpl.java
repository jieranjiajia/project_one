package org.study.aop;

import org.springframework.stereotype.Component;
import org.study.aop.aspect.annotation.CalMethodTime;
import org.study.aop.itf.Greeting;

@Component
public class GreetingImpl implements Greeting {

	@Override
	public void hello(String name) {
		System.out.println("hello-->"+name);
	}

	/**
	 * 使用注解的方法说名需要对该方法做增强处理
	 * @param name
	 */
	@CalMethodTime
	public void sayGoodBye(String name) {
		System.out.println(name + " say goodBye");
		try {
			Thread.sleep(1331);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
