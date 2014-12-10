package com.manzhizhen.extendsimplements;

public class ClassParent {
	private transient String str;
	
	static {
		System.out.println("Parent");
	}
	
	public ClassParent() {
		System.out.println("Parent Constructor");
	}
}
