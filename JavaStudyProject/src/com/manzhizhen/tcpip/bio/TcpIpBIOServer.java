package com.manzhizhen.tcpip.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpIpBIOServer {
	public static void serverTest() {
		ServerSocket serverSocket = null;
		try {
			// 创建对本地指定端口的监听，如果端口冲突，则抛出SocketException，并指定队列里面最多只能存放20个Socket。
			serverSocket = new ServerSocket(1314, 20, InetAddress.getLocalHost());
//			serverSocket.setSoTimeout(60000); 

			// 接收客户端建立连接的请求，并返回Socket对象，以便和客户端进行交互。
			// 交互方式与客户端相同，也是通过Socket.getInputStream和Socket.getOutputStream
			// 来进行读写操作，此方法会一直阻塞到有客户端发送建立连接的请求，如果希望次方法阻塞一定的时间，
			// 则要在创建ServerSocket后调用setSoTimeout设置超时时间
			Socket socket = serverSocket.accept();
			// 创建读取客户端发送流的BufferedReader
			BufferedReader receive = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			// 创建向客户端写入流的PrintWriter
			PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
			// 阻塞读取服务端的返回信息。如果希望一段时间后就不阻塞了，那么要在创建ServerSocket对象后调用其setSoTimeout(毫秒单位的超时时间)
			System.out.println("接收到客户端的信息：" + receive.readLine());
			// 向客户端发送字符串信息
			send.println("I know you are Client!");
			System.out.println("服务端处理完毕！");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null) 
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void server() {
		try {
			TcpIpBIOServerThreadPool.init();

			// 创建对本地指定端口的监听，如果端口冲突，则抛出SocketException。
			ServerSocket serverSocket = new ServerSocket(1314);
//			serverSocket.setSoTimeout(60000); // 这里不设置超时时间，以便可以不停的运行客户端来测试
			
			boolean isDeal;
			// 因为不知道什么时候有会有Socket来连接，所以不断循环
			while(true) {
				// 只有当新的Socket来连接，就会触发accept()
				Socket socket = serverSocket.accept();
				TcpIpBIOServerThread runnable = null;
				isDeal = false;
				for(int i = 0; i < TcpIpBIOServerThreadPool.MAX_TRY_TIMES; i++) {
					if((runnable = TcpIpBIOServerThreadPool.getThread()) == null) {
						Thread.sleep(500); //  如果没获取到空闲的线程，则0.5秒后再尝试获取
						System.out.println(TcpIpBIOServerThreadPool.currentTime() + " 尝试再次获取线程！");
					} else {
						// 给获取到的Socket分配到线程
						runnable.setSocket(socket);
						System.out.println(TcpIpBIOServerThreadPool.currentTime() + " 获取线程成功！");
						isDeal = true;
						break ;
					}
				}
				
				// 如果在尝试了指定次数后，仍然无法获取到空闲的线程，则报超时。
				if(!isDeal) {
					System.err.println(TcpIpBIOServerThreadPool.currentTime() + " 服务端获取Thread超时！");
					continue;
				}
				
				// 处理客户端请求
				new Thread(runnable).start();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		server();
	}
}
