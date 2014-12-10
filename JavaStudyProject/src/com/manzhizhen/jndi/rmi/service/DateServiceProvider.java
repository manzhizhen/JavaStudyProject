/**
 * 
 */
package com.manzhizhen.jndi.rmi.service;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * 本例通过rmi暴露一个服务接口供客户端调用 RMI（Remote Method Invocation）
 * JDK提供的一个完善的、简单易用的远程方法调运框架，它要求服务器端和客户端都是Java程序。
 * 先运行DateServiceProvider.java的main方法，再运行UseDateService.java的main方法
 * 
 * @author Manzhizhen
 * 
 */
public class DateServiceProvider extends UnicastRemoteObject implements IDataService {
	private static final long serialVersionUID = -2332781915833338440L;
	private Map<String, RmiTestData> dataMap = new HashMap<String, RmiTestData>(2);

	public DateServiceProvider() throws RemoteException {
		dataMap.put("manzhizhen", new RmiTestData("manzhizhen", 23));
		dataMap.put("loli", new RmiTestData("loli", 18));
	}
	
	@Override
	public RmiTestData getDataObject(String name) {
		return dataMap.get(name);
	}
	
	/**
	 * 获取服务端系统日期的字符串形式
	 * 
	 * @return
	 */
	@Override
	public String getSysCurrentDateStr() throws RemoteException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 以JNDI的方式注册服务
	 */
	public static void registerServiceForJndi() {
		// 创建和安装一个安全管理器
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			// 创建并导出接受指定 port请求的本地主机上的 Registry 实例。
			LocateRegistry.createRegistry(1314);
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.rmi.registry.RegistryContextFactory");
			System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1314");

			InitialContext ctx = new InitialContext();
			ctx.bind("java:com/manzhizhen/jndi/servicesysCurrentDateService",
					new DateServiceProvider());
			ctx.close();

			System.out.println("服务注册完毕！");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 以RMI的方式注册服务
	 */
	public static void registerServiceForRmi() {
		// 创建和安装一个安全管理器
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new RMISecurityManager());
//		}

		try {
			// 创建并导出接受指定 port请求的本地主机上的 Registry 实例。
			LocateRegistry.createRegistry(1314);
			
//			Naming.bind("//localhost:1314/serviceSysCurrentDateService", new DateServiceProvider());
			Naming.rebind("//localhost:1314/serviceSysCurrentDateService", new DateServiceProvider());
			System.out.println("服务注册完毕！");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
//		catch (AlreadyBoundException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 暴露自己的服务，供外界使用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		registerServiceForJndi();
		registerServiceForRmi();
	}

}
