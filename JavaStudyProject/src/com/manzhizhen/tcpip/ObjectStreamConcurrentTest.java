/**
 * 
 */
package com.manzhizhen.tcpip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 验证ObjectInputStream和ObjectOutputStream并发的影响。
 * @author Manzhizhen
 *
 */
public class ObjectStreamConcurrentTest {
	private static Socket socket;
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) throws InterruptedException, IOException{
		// 客户端1发送消息和接收消息
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socket = new Socket(InetAddress.getLocalHost(), 1313);
//					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					while(true) {
						objectOutputStream.writeObject(new String("客户端1发送的消息！"));
						objectOutputStream.flush();
						System.out.println("客户端1消息发送完毕！");
//						Object obj = objectInputStream.readObject();
//						System.out.println("客户端1接收：" + obj);
						Thread.sleep(3000);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} 
//				catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}).start();
		
//		// 客户端2发送消息和接收消息
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//					while(true) {
//						objectOutputStream.writeObject(new String("客户端2发送的消息！"));
//						objectOutputStream.flush();
//						Object obj = objectInputStream.readObject();
//						System.out.println("客户端2接收：" + obj);
//						Thread.sleep(3000);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} 
//			}
//		}).start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			reader.readLine();
		}
	}
	
	
}
