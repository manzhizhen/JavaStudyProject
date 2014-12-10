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
public class ObjectStreamConcurrentServerTest {
	private static Socket socket;
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) throws InterruptedException, IOException{
		// 服务端接收消息和应答消息
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(1313);
					while(true) {
						final Socket socket = serverSocket.accept();
						new Thread(new Runnable() {
							@Override
							public void run() {
								ObjectInputStream objectInputStream;
//								ObjectOutputStream objectOutputStream;
								try {
									objectInputStream = new ObjectInputStream(socket.getInputStream());
//									objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
									while(true) {
										Object obj = objectInputStream.readObject();
										System.out.println("服务端接收：" + obj);
//										objectOutputStream.writeObject(new String("I miss you"));
//										objectOutputStream.flush();
									}
								} catch (IOException e) {
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			reader.readLine();
		}
	}
	
	
}
