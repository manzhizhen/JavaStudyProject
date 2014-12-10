package com.manzhizhen.wrapper;

public class TestInteger {
	public static void main(String[] args) {
		Integer i1 = new Integer(123);
		Integer i2 = new Integer(123);
		Integer i3 = 123;
		Integer i4 = 123;
		
		System.out.println(i3.toString());
		System.out.println(i4.toString());
		System.out.println(i3 == i4);
		System.out.println(i1 == i2);
		
		System.out.println("1234567890".substring(0, 8));
	}
}
