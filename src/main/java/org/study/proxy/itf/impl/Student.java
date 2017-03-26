package org.study.proxy.itf.impl;

import org.study.proxy.itf.Person;

public class Student implements Person {
	
	@Override
	public void talk(String name, String speek) {
		System.out.println(name+"做了"+speek+"的演讲");
	}

	@Override
	public void walk(int miles) {
		System.out.println("我一共走了"+miles+"里路！！！");
	}
}
