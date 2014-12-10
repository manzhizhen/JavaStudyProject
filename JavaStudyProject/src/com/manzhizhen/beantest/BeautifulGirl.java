package com.manzhizhen.beantest;

public class BeautifulGirl {
	private String name;
	private int age;
	private int weight;
	private String iPhoneNum;
	private String QQNum;
	
	public BeautifulGirl() {
	}
	
	public BeautifulGirl(String name, int age, int weight) {
		this.name = name;
		this.age = age;
		this.weight = weight;
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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public String getiPhoneNum() {
		return iPhoneNum;
	}

	public void setiPhoneNum(String iPhoneNum) {
		this.iPhoneNum = iPhoneNum;
	}

	public String getQQNum() {
		return QQNum;
	}

	public void setQQNum(String qQNum) {
		QQNum = qQNum;
	}

}
