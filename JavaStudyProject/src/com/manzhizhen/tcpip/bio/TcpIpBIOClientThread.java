package com.manzhizhen.tcpip.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端线程
 * 
 * @author Manzhizhen
 * 
 */
public class TcpIpBIOClientThread implements Runnable {
	private SocketData socketData;
	private Socket socket = null;

	public TcpIpBIOClientThread(SocketData socketData) {
		this.socketData = socketData;
	}

	@Override
	public void run() {
		try {
			boolean isGet = false;
			for(int i = 0; i < TcpIpBIOClientSocketPool.MAX_TRY_TIMES; i++) {
				if((socket = TcpIpBIOClientSocketPool.getSocket()) == null) {
					Thread.sleep(1000); //  如果没获取到空闲的Socket，则1秒后再尝试获取
					System.out.println(TcpIpBIOClientSocketPool.currentTime() + this + " 尝试再次获取Socket！");
				} else {
					System.out.println(TcpIpBIOClientSocketPool.currentTime() + this + " 成功获取Socket！");
					isGet = true;
					break ;
				}
			}

			if(!isGet) {
				System.err.println(TcpIpBIOClientSocketPool.currentTime() + " 客户端获取Socket超时！");
				return ;
			}
			
//			socket.setSoTimeout(TcpIpBIOClientSocketPool.TIMEOUT);
			// 创建向服务器写入流的PrintWriter
			PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
			// 创建读取服务器端返回流的BufferedReader
			BufferedReader receive = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			// 向服务器发送数据对象
			send.println(socketData.getStrFromObj());
			// 阻塞读取服务端的返回信息。如果希望一段时间后就不阻塞了，那么要在创建Socket对象后调用socket.setSoTimeout(毫秒单位的超时时间)
			socketData = SocketData.getObjFromStr(receive.readLine());
			System.out.println(TcpIpBIOClientSocketPool.currentTime()  + this + " 客户端接收完毕：" + socketData);
		
		} catch (IOException e) {
			System.err.println(TcpIpBIOClientSocketPool.currentTime()  + this + " 客户端异常：" + socketData);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println(TcpIpBIOClientSocketPool.currentTime()  + this + " 客户端异常：" + socketData);
			e.printStackTrace();
		} finally  {
			// 释放客户端那有限的Socket资源
			TcpIpBIOClientSocketPool.freeSocket(socket);
		}
	}

	public SocketData getSocketData() {
		return socketData;
	}

	public Socket getSocket() {
		return socket;
	}
}
