package com.manzhizhen.tcpip.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * 服务端处理Socket的线程
 * 
 * @author Administrator
 * 
 */
public class TcpIpBIOServerThread implements Runnable {
	private SocketData socketData = new SocketData();
	private Socket socket = null;

	public TcpIpBIOServerThread() {
	}

	@Override
	public void run() {
		try {
//			socket.setSoTimeout(TcpIpBIOServerThreadPool.READ_TIMEOUT);
			
			// 创建读取客户端发送的BufferedReader
			BufferedReader read = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			// 创建给客户端响应写入流的PrintWriter
			PrintWriter sendOut = new PrintWriter(socket.getOutputStream(), true);
			
			// 阻塞读取客户端的发送的信息。如果希望一段时间后就不阻塞了，那么要在创建Socket对象后调用socket.setSoTimeout(毫秒单位的超时时间)
			String readStr = null;
			while(true) {
				// 一旦客户端项目的TcpIpBIOClient.java的main方法运行完，客户端会失去响应，
				// 此时下面的语句会抛出java.net.SocketException: Connection reset异常
				// 为了避免这样发生，可以在客户端的main方法加入sleep()让客户端进程睡眠。
				readStr = read.readLine();
				if(readStr != null) {
					socketData = SocketData.getObjFromStr(readStr);
					// 服务端处理数据
					dealData(socketData);
					
					// 向客户端发送响应数字信息
					sendOut.println(socketData.getStrFromObj());
					System.out.println(TcpIpBIOServerThreadPool.currentTime() + " 服务端处理完毕完毕：" + socketData);
				}
			}
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(TcpIpBIOServerThreadPool.currentTime() + " 服务端异常：" + socketData);
			e.printStackTrace();
		} finally {
			// 使用完后释放自己
			TcpIpBIOServerThreadPool.freeThread(this);
			// 注意，这里不必关闭Socket，因为这个Socket是客户端Socket连接池维护的，
			// 如果这时候关了，客户端就无法通过这个Socket来和服务端通信了
		}
	}

	/**
	 * 对数据进行业务处理方法
	 * @param socketData2
	 */
	private void dealData(SocketData socketData) {
		// 将客户端发送过来的数据进行简单处理
		socketData.setServerNum(socketData.getClientNum() * 2);
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
