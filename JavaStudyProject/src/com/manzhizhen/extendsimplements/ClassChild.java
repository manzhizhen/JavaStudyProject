package com.manzhizhen.extendsimplements;

public class ClassChild extends ClassParent {
	static {
		System.out.println("Child");
	}

	public static class B {
	}

	public class C {
	}
	
	public ClassChild() {
		System.out.println("Child Constructor");
	}

	public static void main(String[] args) {
//		new ClassChild();
		
//		int i = 9;
//		switch (i) {
//		case 1:
//			System.out.print("");
//		default:
//			System.out.print("Error");
//		case 2:
//			System.out.print("Good");
//		case 3:
//			System.out.print("Best");
//		}
		
//		ClassChild.B b = new ClassChild.B();
//		
//		ClassChild child = new ClassChild();
//		ClassChild.C c = child.new C();
		
//		System.out.print("a".toUpperCase().valueOf('b'));
		System.out.println((0.4-0.3) == (0.2-0.1));
	}
}
