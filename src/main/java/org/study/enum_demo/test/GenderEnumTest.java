package org.study.enum_demo.test;

import org.study.enum_demo.Gender;

public class GenderEnumTest {
	
	public static void main(String[] args) {
		//枚举可以用Enum获取枚举的实例
		Gender gender = Enum.valueOf(Gender.class, "MALE");
		String name = gender.getName();
		System.out.println(name);
		
		//默认的values方法遍历所有的枚举值
		Gender[] values = Gender.values();
		for (Gender g : values) {
			System.out.println(g);
		}
		
		Gender female = Gender.FEMALE;
		female.info();
	}
}
