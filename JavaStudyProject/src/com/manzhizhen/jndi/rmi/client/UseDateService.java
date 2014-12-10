/**
 * 
 */
package com.manzhizhen.jndi.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 本例调用DateServiceProvider类通过rmi或jndi暴露的服务
 * RMI（Remote Method Invocation）
 * JDK提供的一个完善的、简单易用的远程方法调运框架，它要求服务器端和客户端都是Java程序。
 * 先运行DateServiceProvider.java的main方法，在运行本例的main方法
 * @author Manzhizhen
 *
 */
public class UseDateService {
	/**
	 * 当DateServiceProvider通过registerServiceForJndi()注册服务时，使用此方法获取服务
	 */
	public static void getServiceFromJndi() {
		InitialContext ctx;
		try {
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.rmi.registry.RegistryContextFactory");
			System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1314");
			ctx = new InitialContext();
			
			IDataService dataService = (IDataService) ctx.lookup("java:com/manzhizhen/jndi/servicesysCurrentDateService");  
			System.out.println("服务端系统日期：" + dataService.getSysCurrentDateStr());
			System.out.println("服务端数据对象：" + dataService.getDataObject("manzhizhen"));
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 当DateServiceProvider通过registerServiceForRmi()注册服务时，使用此方法获取服务
	 */
	public static void getServiceFromRmi() {
		try {
			IDataService dataService = (IDataService)Naming.lookup("//localhost:1314/serviceSysCurrentDateService");
			System.out.println("服务端系统日期：" + dataService.getSysCurrentDateStr());
			System.out.println("服务端数据对象：" + dataService.getDataObject("loli"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		getServiceFromRmi();
		System.out.println("over");
	}

}
