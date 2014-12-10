/**
 * 
 */
package com.manzhizhen.hessian.client;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * 直接使用Hessian
 * 
 * @author Manzhizhen
 * 
 */
public class HessianClientTest {
	public static void main(String[] args) {
		String url = "http://localhost:8090/JavaStudyWebProject/Hessian";
		HessianProxyFactory factory = new HessianProxyFactory();
		try {
			IMyHessianService myHessianService = (IMyHessianService) factory
					.create(IMyHessianService.class, url);
			System.out.println(myHessianService.getDataObject("01"));
			System.out.println(myHessianService.getDataObject("02"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
