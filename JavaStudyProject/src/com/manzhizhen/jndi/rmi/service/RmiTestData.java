/**
 * 
 */
package com.manzhizhen.jndi.rmi.service;

import java.io.Serializable;

/**
 * RMI的测试数据对象
 * 
 * @author Manzhizhen
 * 
 */
public class RmiTestData implements Serializable{
	private static final long serialVersionUID = 5372242778998505379L;
	
	private String name;
	private int age;
	
	/*
	 * 注意，客户端的RmiTestData对象可没有这个构造方法
	 */
	public RmiTestData(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "这是服务端的RmiTestData：" + name + " " + age;
	}
}
