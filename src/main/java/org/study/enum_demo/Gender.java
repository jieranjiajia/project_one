package org.study.enum_demo;
import static java.lang.System.*;
/**
 * 定义一个性别的枚举类
 * 枚举类是中特殊的类
 * 1.枚举常量必须方法最前面，每个常量之间用应为的逗号隔开，并且最后以英文的分好结束
 * 2.枚举常量一样可以有实例变量和方法
 * 3.枚举默认继承java.lang.Enum类而不是Object类，因此枚举类不能显示的再继承他气的父类
 * 4.枚举类默认提供了一个values()的方法。该方法可以很方便的遍历所有的枚举值
 * 
 * 
 * 注意：===========================
 * 枚举的意义就是不能让外部随便更改设置，换句话来说用private把构造器隐藏起来。把这个类所有可能的实例都用
 * public static final 修饰的类变量来保存
 * 
 */
public enum Gender implements GenderDesc{
	MALE("男"){

		@Override
		public void info() {
			out.print("男生负责赚钱养家");
		}
		
	},FEMALE("女"){

		@Override
		public void info() {
			out.print("女生负责貌美如花");
		}
		
	};
	
	private String name;
	
	private Gender(String name) {
		this.name = name;
	}
	
	public String getName (){
		return this.name;
	}
	
}
