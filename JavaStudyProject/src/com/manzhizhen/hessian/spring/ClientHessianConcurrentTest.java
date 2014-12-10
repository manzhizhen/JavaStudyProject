/**
 * 
 */
package com.manzhizhen.hessian.spring;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Manzhizhen
 *
 */
@Service("hessianConcurrentTest")
public class ClientHessianConcurrentTest {
	private final static int MAX_THREAD = 3000;
	
	@Autowired
	private ITestService hessianRemoting;
	
	public static void main(String[] args) {
		ApplicationContext applicatonContext = new ClassPathXmlApplicationContext("**/clientApplicationContext.xml");
		ClientHessianConcurrentTest hessianConcurrentTest = (ClientHessianConcurrentTest) applicatonContext.getBean("hessianConcurrentTest");

		Runnable[] threads = new Runnable[MAX_THREAD];
		for(int i = 0; i <  MAX_THREAD; i++) {
			threads[i] = new TestThread(hessianConcurrentTest.hessianRemoting);
		}
		
		System.out.println("消息发送时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date()));
		for(int i = 0; i < MAX_THREAD; i++) {
			new Thread(threads[i]).start();
		}
		
		System.out.println("over!!!");
		
	}
}
