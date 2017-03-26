package org.study.enum_demo;

public enum Operation {
	
	PLUS{
		public double eval(double a, double b) {
			return a + b;
		}
	},
	MINUS {
		public double eval(double a, double b) {
			return a - b;
		}
	},
	TIMES {
		public double eval(double a, double b) {
			return a * b;
		}
	},
	DEVIDE {
		public double eval(double a, double b) {
			return a / b;
		}
	}; 
	
	//为Operation枚举类定一个抽象发，具体实现有各个枚举值实现
	public abstract double eval(double a, double b);
	
	
	public static void main(String[] args) {
		System.out.println(Operation.DEVIDE.eval(10, 23));
		System.out.println(Operation.PLUS.eval(11, 22));
		System.out.println(Operation.MINUS.eval(5, 90));
		System.out.println(Operation.TIMES.eval(34, 234));
	}
}
