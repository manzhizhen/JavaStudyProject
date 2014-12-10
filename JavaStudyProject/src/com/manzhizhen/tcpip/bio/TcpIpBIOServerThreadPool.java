package com.manzhizhen.tcpip.bio;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * 服务端线程连接池
 * @author Administrator
 *
 */
public class TcpIpBIOServerThreadPool {
	public static int MAX_POOL_NUM = 50; // 连接池数量
	public static int ACCEPT_TIMEOUT = 99999999; // 设置ServerSocket监听的超时时间
	public static int READ_TIMEOUT = 60000; // 设置Socket读取数据的超时时间为60秒
	
	public static int MAX_TRY_TIMES = 20; // 如果服务端没有获取到空闲的线程，尝试获取线程的次数
	private static boolean INIT_STATUS = false; // 连接池的初始化状态
	
	public static Map<TcpIpBIOServerThread, Boolean> threadMap = new Hashtable<TcpIpBIOServerThread, Boolean>(MAX_POOL_NUM); // true表示该线程可以使用
	
	private TcpIpBIOServerThreadPool() {
	}
	
	/**
	 * 初始化线程连接池
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void init() throws UnknownHostException, IOException {
		if(INIT_STATUS) {
			return ;
		}
		for(int i = MAX_POOL_NUM; i >= 0; i--) {
			TcpIpBIOServerThread runnable = new TcpIpBIOServerThread();
			threadMap.put(runnable, true);
		}
		
		// 如果没抛异常，设置初始化状态为true
		INIT_STATUS = true;
	}
	
	/**
	 * 释放一个线程，该线程可以供其他请求使用
	 * @param socket
	 */
	public static void freeThread(TcpIpBIOServerThread runnable) {
		if(!INIT_STATUS) {
			System.out.println("连接池未初始化！");
			return ;
		}
		
		if(runnable == null) {
			return ;
		}
		
		if(threadMap.get(runnable) != null) {
			System.out.println(currentTime() + runnable + " 释放线程成功！ ");
			threadMap.put(runnable, true);
		}
	}
	
	/**
	 * 获得一个可用的线程，如果没有可用线程，则返回null
	 * @param socket
	 */
	public static TcpIpBIOServerThread getThread() {
		if(!INIT_STATUS) {
			System.out.println("连接池未初始化！");
			return null;
		}

		for(TcpIpBIOServerThread runnable : threadMap.keySet()) {
			if(threadMap.get(runnable)) {
				threadMap.put(runnable, false); 
				return runnable;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String currentTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
}
