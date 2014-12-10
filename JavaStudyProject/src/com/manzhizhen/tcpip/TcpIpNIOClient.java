package com.manzhizhen.tcpip;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class TcpIpNIOClient {
	public static void main(String[] args) {
		try {
			SocketChannel channel = SocketChannel.open();
			// 设置成非阻塞模式
			channel.configureBlocking(false);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
