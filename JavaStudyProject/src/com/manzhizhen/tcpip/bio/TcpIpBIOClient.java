package com.manzhizhen.tcpip.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端的入口
 * TCP/IP+BIO
 * 基于Java自身包实现消息方式的系统间通信的方式有四种：
 * TCP/IP+BIO TCP/IP+NIO UDP/IP+BIO UDP/IP+NIO
 * @author Manzhizhen
 *
 */

public class TcpIpBIOClient {
	public static void clientTest() {
		Socket socket = null;
		try {
			// InetAddress.getLocalHost()为服务端IP地址,因为服务器就部署在本地，1314为服务端开启的端口号
			socket = new Socket(InetAddress.getLocalHost(), 1314);
//			socket.setSoTimeout(60000); // 设置超时时间为60秒
			// 创建向服务器写入流的PrintWriter
			PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
			// 创建读取服务器端返回流的BufferedReader
			BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 向服务器发送字符串信息
			send.println("Is me, Client!!");
			// 阻塞读取服务端的返回信息。如果希望一段时间后就不阻塞了，那么要在创建Socket对象后调用socket.setSoTimeout(毫秒单位的超时时间)
			System.out.println("接收到服务端的响应："  + receive.readLine());
			System.out.println("客户端处理完毕！");
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void client() {
		try {
			// 初始化Socket连接池
			TcpIpBIOClientSocketPool.init();
			// 自增用来作为发送的数据
			int startNum = 10000;
			
			// 模拟300个并发请求
			for(int i = 0; i < 300; i++) {
				SocketData socketData = new SocketData();
				socketData.setClientNum(startNum + i);
				
				// 每次请求都会创建一个新的线程
				Runnable runnable = new TcpIpBIOClientThread(socketData);
				new Thread(runnable).start();
			}
			
			try {
				// 处理完休息一下
				Thread.sleep(9999999);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		client();
	}
}
