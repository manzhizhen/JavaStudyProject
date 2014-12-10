package com.manzhizhen.tcpip.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * 客户端Socket连接池
 * @author Manzhizhen
 *
 */
public class TcpIpBIOClientSocketPool {
	private static int MAX_POOL_NUM = 50; // Socket连接池中Socket最大数量
	public static int TIMEOUT = 60000; // 设置超时间为60秒
	public static int MAX_TRY_TIMES = 10; // 如果客户端没有获取到空闲的Scoket，尝试获取Scoket的次数
	private static boolean INIT_STATUS = false; // 连接池的初始化状态
	
	private static InetAddress IP_ADDRESS;	// 服务器端IP地址
	private static int PORT;				// 服务器端口号
	
	public static Map<Socket, Boolean> socketMap = new Hashtable<Socket, Boolean>(MAX_POOL_NUM); // true表示该Socket可以使用
	
	private TcpIpBIOClientSocketPool() {
	}
	
	/**
	 * 初始化Socket连接池
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public synchronized static void init() throws UnknownHostException, IOException {
		if(INIT_STATUS) {
			return ;
		}
		
		IP_ADDRESS = InetAddress.getLocalHost(); 	// 设置服务器端IP地址，因为服务器就在本地，所以使用本地IP地址。
		PORT = 1314;								// 设置服务器端口号
		
		for(int i = MAX_POOL_NUM; i > 0; i--) {
			Socket socket = new Socket(IP_ADDRESS, PORT);
			socketMap.put(socket, true);
		}
		
		// 如果没抛异常，设置初始化状态为true
		INIT_STATUS = true;
	}
	
	/**
	 * 释放一个Socket，该Socket可以供其他客户端使用
	 * @param socket
	 */
	public static synchronized void freeSocket(Socket socket) {
		if(!INIT_STATUS) {
			System.out.println("连接池未初始化！");
			return ;
		}
		
		if(socket == null) {
			return ;
		}
		
		if(socketMap.get(socket) != null) {
			socketMap.put(socket, true);
			System.out.println(TcpIpBIOClientSocketPool.currentTime() + " 已经释放了一个Socket");
		}
	}
	
	/**
	 * 获得一个可用的Socket，如果没有可用Socket，则返回null
	 * @param socket
	 */
	public static synchronized Socket getSocket() {
		if(!INIT_STATUS) {
			System.out.println("连接池未初始化！");
			return null;
		}

		for(Socket socket : socketMap.keySet()) {
			if(socketMap.get(socket)) {
				socketMap.put(socket, false); 
				return socket;
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
