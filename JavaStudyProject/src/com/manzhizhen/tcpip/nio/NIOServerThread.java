package com.manzhizhen.tcpip.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.validation.constraints.AssertTrue;

public class NIOServerThread implements Runnable {
	private Selector selector;

	public NIOServerThread(Selector selector) {
		assert selector != null;
		this.selector = selector;
	}

	@Override
	public void run() {
		try {
			while (true) {
				// 选择一组键，其相应的通道已为 I/O 操作准备就绪。返回已更新其准备就绪操作集的键的数目，该数目可能为0.
				int num = selector.select();
				System.out.println("准备就绪的操作集的键的数目：" + num);

				// 返回此选择器的键集。
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iter = keys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();

					// 此键的通道是否已准备好接受新的套接字连接。
					if (key.isAcceptable()) {
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
								.channel();
						SocketChannel socketChannel = serverSocketChannel
								.accept();
						Socket socket = socketChannel.socket();
						if (1314 == socket.getPort()) {
							System.out.println("接受了一个注册用户发来的连接!");
							
						} else if (11314 == socket.getPort()) {
							System.out.println("接受了一个登录用户发来的连接!");
							
						} else {
							System.out.println("接受了一个未知用户发来的连接!");
						}

						// 设置非阻塞模式
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ,
								ByteBuffer.allocate(1024));

						// 此键的通道是否已准备好进行读取。
					} else if (key.isReadable()) {
						// 获得与客户端通信的信道
						SocketChannel clientChannel = (SocketChannel) key
								.channel();

						// 获取当前的附加对象，即缓冲区。
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						buffer.clear();

						// 读取信息获得读取的字节数
						long bytesRead = clientChannel.read(buffer);

						if (bytesRead == -1) {
							// 没有读取到内容的情况
							clientChannel.close();
						} else {
							// 反转此缓冲区。首先将限制设置为当前位置，然后将位置设置为 0。 
							// 在一系列通道读取或放置 操作之后，调用此方法为一系列通道写入或相对获取 操作做好准备。
							buffer.flip();

							// 将字节转化为为UTF-8的字符串
							String receivedString = Charset.forName("UTF-8")
									.newDecoder().decode(buffer).toString();

							// 控制台打印出来
							System.out.println("接收到注册消息"
									+ clientChannel.socket()
											.getRemoteSocketAddress() + "的信息:"
									+ receivedString);

							// 准备发送的文本
							String sendString = "你好,客户端. @"
									+ new Date().toString() + "，已经收到你的信息"
									+ receivedString;
							buffer = ByteBuffer.wrap(sendString
									.getBytes("UTF-16"));
							clientChannel.write(buffer);

							// 设置为下一次读取或是写入做准备
							key.interestOps(SelectionKey.OP_READ
									| SelectionKey.OP_WRITE);
						}
					}

					iter.remove(); // 处理完事件的要从keys中删去，为了提高效率，选择最后一次性清空。
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
