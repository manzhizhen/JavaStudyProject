package com.manzhizhen.tcpip.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 客户端的入口
 * TCP/IP+NIO
 * 基于Java自身包实现消息方式的系统间通信的方式有四种：
 * TCP/IP+BIO TCP/IP+NIO UDP/IP+BIO UDP/IP+NIO
 * @author Manzhizhen
 *
 */

public class TcpIpNIOClient {
	public static void clientTest() {
		try {
			// 初始化选择器
			Selector selector = Selector.open();
			
			SocketChannel channel = SocketChannel.open();
			// 设置为非阻塞模式
			channel.configureBlocking(false);
			
			// 对于非阻塞模式，立刻返回false，表示连接正在建立中。
			channel.connect(InetSocketAddress.createUnresolved("0.0.0.0", 1314));
			// 由于connect方法的立即返回，所以我们需要调用如下方法检验连接是否真正建立
			while(!channel.finishConnect()) {
				Thread.sleep(500);
			}
			

			// 向channel注册selector以及感兴趣的连接事件
			channel.register(selector, SelectionKey.OP_CONNECT);
			
			// 阻塞至有感兴趣的IO事件发生，或到达超时时间，如果希望一直等至有兴趣的IO事件发生，
			// 可调用无参数的select方法，如果希望不阻塞直接返回目前是否有感兴趣的事件发生，
			// 可调用selectNow方法
			int nKeys = selector.select(60000); // 设置超时时间为60秒
			
			// 如果nKeys大于零，说明有感兴趣的IO事件发生
			SelectionKey sKey = null;
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			if(nKeys > 0) {
				Set<SelectionKey> keys = selector.selectedKeys();
				for(SelectionKey key : keys) {
					// 对于发生连接的事件
					if(key.isConnectable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						sc.configureBlocking(false);
						
						// 注册感兴趣的IO读事件，通常不直接注册写事件，在发送缓冲区未满的情况下，
						// 一直是可写的，因此如果注册了写事件，而又不用写数据，很容易造成CPU消耗100%的现象
						sKey = sc.register(selector, SelectionKey.OP_READ);
						// 完成连接的简历
						sc.finishConnect();
					
					// 有流可以读取
					} else if(key.isReadable()) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						SocketChannel sc = (SocketChannel) key.channel();
						int readBytes = 0;
						try {
							int ret = 0;
							try {
								// 读取目前可读的流，sc.read返回的为成功复制到bytebuffer
								// 中的字节数，此步为阻塞操作，值可能为0；当已经是流的结尾时，返回-1。
								while((ret = sc.read(buffer)) > 0) {
									readBytes += ret;
								}
							} finally {
								buffer.flip();
							}
						} finally {
							if(buffer != null) {
								buffer.clear();
							}
						}
					// 可写入流
					} else if(key.isWritable()) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						// 取消对OP_WRITE事件的注册
						key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
						SocketChannel sc = (SocketChannel) key.channel();
						
						// 此步为阻塞操作，直到写入操作系统发送缓冲区或网络IO出现异常，
						// 返回的为成功写入的字节数，当操作系统的发送缓冲区已满，此处会返回0
						int writtenedsize = sc.write(buffer);
						// 如未写入，则继续注册感兴趣的OP_WRITE事件
						if(writtenedsize == 0) {
							key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
						}
					}
				}
				
				selector.selectedKeys().clear();
			}
			
//			// 对于要写入的流，可直接调用channel.write来完成，
//			// 只有在写入未成功时才要注册OP_WRITE事件
//			int wSize = channel.write(byteBuffer);
//			if(wSize == 0) {
//			
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		clientTest();
		try {
			SocketChannel channel = SocketChannel.open();
			// 设置为非阻塞模式
			channel.configureBlocking(false);
			// 对于非阻塞模式，立刻返回false，表示连接正在建立中。
			System.out.println(channel.connect(InetSocketAddress.createUnresolved("0.0.0.0", 1314)));
			System.out.println(channel.isConnected());
			System.out.println(channel.finishConnect());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
