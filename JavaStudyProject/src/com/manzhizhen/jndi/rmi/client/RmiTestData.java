/**
 * 
 */
package com.manzhizhen.jndi.rmi.client;

import java.io.Serializable;

/**
 * RMI的测试数据对象
 * 这个对象是由服务端的方法返回的，所以必须是序列化的
 * 这个数据对象服务端和客户端都有，且serialVersionUID值需要一样
 * 而且需要注意的是，客户端和服务端这个对象的包名需一致。
 * 
 * @author Manzhizhen
 * 
 */
public class RmiTestData implements Serializable{
	
	private static final long serialVersionUID = 5372242778998505379L;
	
	private String name;
	private int age;

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
		return "这是客户端的RmiTestData：" + name + " " + age;
	}
}
