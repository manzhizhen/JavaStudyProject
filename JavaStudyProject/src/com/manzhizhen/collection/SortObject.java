package com.manzhizhen.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Arrays.sort(T[] a, Comparator<? super T> c) 用于给数组排升序
 * Collections.sort(List<T> a, Comparator<? super T> c) 用于给列表排升序
 * @author Administrator
 *
 */
public class SortObject {
	private String name;
	
	public SortObject(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public static void main(String[] args) {
		SortObject[] sortArray = new SortObject[]{new SortObject("java"), new SortObject("c++"), 
				new SortObject("c"), new SortObject("c#"), new SortObject("javascript"),};
		Arrays.sort(sortArray, new Comparator<SortObject>() {
			@Override
			public int compare(SortObject o1, SortObject o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		System.out.println(Arrays.toString(sortArray));
		
		
		sortArray = new SortObject[]{new SortObject("java"), new SortObject("c++"), 
				new SortObject("c"), new SortObject("c#"), new SortObject("javascript"),};
		List<SortObject> sortList = Arrays.asList(sortArray);
		Collections.sort(sortList,new Comparator<SortObject>() {
			@Override
			public int compare(SortObject o1, SortObject o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		
	}
}
