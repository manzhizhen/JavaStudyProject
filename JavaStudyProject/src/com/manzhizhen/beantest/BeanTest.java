package com.manzhizhen.beantest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanTest {
	public static void main(String[] args) {
		// 父容器
		ClassPathXmlApplicationContext pFactory = new ClassPathXmlApplicationContext(
				new String[]{"com/manzhizhen/beantest/boygirl-config.xml"});
		
		BeautifulGirl beautifulGirl = (BeautifulGirl) pFactory.getBean("girl1");
		System.out.println(beautifulGirl.getAge());
	}
}
