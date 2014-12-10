package com.manzhizhen.collection;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
	public static void main(String[] args) {
		List<String> strList = new ArrayList<String>(10);
		System.out.println(strList.size());
		strList.add(null);
		System.out.println(strList.size());
		strList.add(1, "1");
		strList.add(2, "2");
//		strList.remove("2");
//		System.out.println(strList.size());
	}
}
