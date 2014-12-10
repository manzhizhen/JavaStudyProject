/**
 * 
 */
package com.manzhizhen.hessian;

import java.io.Serializable;

/**
 * 服务端和客户端所使用的数据对象
 * 注意，由于Hessian是通过对象序列化来传输的，
 * 也就是说如果接口的返回值是一个自定义对象时（比如这里的HessianData），
 * 则需要客户端和服务端的这个自定义对象所属包名相同，
 * 但并不要求客户端和服务端的HessianData类一模一样，
 * 比如服务端和客户端的HessianData可以实现自己的toString()方法。
 * 
 * @author Manzhizhen
 * 
 */
public class HessianData implements Serializable {
	private static final long serialVersionUID = -2123351112061698812L;
	
	private String id;
	private String name;
	private int age;
	
	public HessianData(String id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return id + " " + name + " " + age;
	}

}
