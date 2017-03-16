package org.study.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.stereotype.Component;
import org.study.aop.itf.Apology;
/**
 * 使用spring实现引入增强
 * @author ou_qu_sheng
 *
 */
@Component
public class GreetingIntroAdvice extends DelegatingIntroductionInterceptor implements Apology {

	private static final long serialVersionUID = 1L;

	
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		return super.invoke(mi);
	}

	@Override
	public void sorry(String name) {
		System.out.println("spring 的引入增强 DelegatingIntroductionInterceptor 被执行");
		System.out.println("sorry -- " + name);
	}

}
