package com.manzhizhen.collection;

import java.util.Vector;

public class TestVector {
	public static void main(String[] args) {
		Vector<String> strVector = new Vector<String>(100);
		strVector.add("1");
		strVector.add("2");
		strVector.add("3");
		strVector.add(null);
		strVector.add("5");
		strVector.add(null);
		
		strVector.remove(2);
		System.out.println(strVector.get(4));
		
		System.out.println("size: " + strVector.size());
		System.out.println("capacity: " + strVector.capacity());
		
	}
}
