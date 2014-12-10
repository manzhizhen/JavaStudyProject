package com.manzhizhen.function;

public class MutiParTest {
	public static void test1(String str, int... type) {
		System.out.println((type != null && type.length > 0) ? "test1 : hello " : "test1 : null");	
//		test2(str, type);
	}
	
	public static void test2(String str, int... type2) {
		System.out.println(type2.length > 0 ? "test2 : hello " : "test2 : null");
	}
	
	public static void main(String[] args) {
		test1("123", null);
		test1("123");
	}
}
