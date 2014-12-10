package com.manzhizhen.tcpip.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class TcpIpNIOServer {
	public static void serverTest() {
		try {
			// 初始化选择器,该方法将使用系统的默认选择器提供者创建新的选择器。
			Selector selector = Selector.open();

			// 打开"注册"通道，ServerSocketChannel是一个基于通道的Socket监听器。
			// 它和我们所熟悉的java.net.ServerSocket执行相同的基本任务，不过它增加了通道语义，因此能够在非阻塞模式下运行。
			// 用静态的open()工厂方法创建一个新的ServerSocketChannel对象，将会返回同一个未绑定的java.net.ServerSocket关联的通道。
			ServerSocketChannel regSSC = ServerSocketChannel.open();
			// 打开"登录"通道
			ServerSocketChannel logSSC = ServerSocketChannel.open();

			// 设置成非阻塞模式
			regSSC.configureBlocking(false);
			logSSC.configureBlocking(false);

			// 获得ServerSocket
			ServerSocket regServerSocket = regSSC.socket();
			ServerSocket logServerSocket = logSSC.socket();

			// 绑定要监听的端口
			regServerSocket.bind(new InetSocketAddress(1314));
			logServerSocket.bind(new InetSocketAddress(11314));

			// 注册感兴趣的事件。
			// 通过某个通道的 register方法注册该通道时，所带来的副作用是向选择器的键集中添加了一个键.
			regSSC.register(selector, SelectionKey.OP_ACCEPT);
			logSSC.register(selector, SelectionKey.OP_ACCEPT);

			new Thread(new NIOServerThread(selector)).start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// serverTest();
		// 打开通道
		ServerSocketChannel ssc;
		ServerSocketChannel ssc1;
		try {
			ssc = ServerSocketChannel.open();
			ssc1 = ServerSocketChannel.open();
			ServerSocket serverSocket = ssc.socket();
			ServerSocket serverSocket1 = ssc1.socket();
			System.out.println(serverSocket);
			System.out.println(serverSocket1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
