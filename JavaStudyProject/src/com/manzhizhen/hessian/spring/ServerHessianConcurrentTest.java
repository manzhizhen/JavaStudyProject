/**
 * 
 */
package com.manzhizhen.hessian.spring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

/**
 * @author Manzhizhen
 *
 */
//@Service("hessianConcurrentTest")
public class ServerHessianConcurrentTest {
	@Autowired
	private ITestService hessianRemoting;
	
	public static void main(String[] args) {
		ApplicationContext applicatonContext = new ClassPathXmlApplicationContext("serverApplicationContext.xml");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			while(true) 
				System.out.println(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
}
