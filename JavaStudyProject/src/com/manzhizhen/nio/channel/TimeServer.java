/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * 本例是一个RFC 868 时间服务器。这段代码回答来自TimeClient.java的客户端的请求并
显示出 DatagramChannel是怎样绑定到一个熟知端口然后开始监听来自客户端的请求的。该时间服
务器仅监听数据报（UDP）请求。大多数Unix 和Linux 系统提供的rdate命令使用TCP 协议连接
到一个 RFC 868时间服务。
 * @author Manzhizhen
 * 
 */
public class TimeServer {
	private static final int DEFAULT_TIME_PORT = 37;
	private static final long DIFF_1900 = 2208988800L;
	protected DatagramChannel channel;

	public TimeServer(int port) throws Exception {
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(new InetSocketAddress(port));
		System.out.println("Listening on port " + port + " for time requests");
	}

	public void listen() throws Exception {
		// Allocate a buffer to hold a long value
		ByteBuffer longBuffer = ByteBuffer.allocate(8);
		// Assure big-endian (network) byte order
		longBuffer.order(ByteOrder.BIG_ENDIAN);
		// Zero the whole buffer to be sure
		longBuffer.putLong(0, 0);
		// Position to first byte of the low-order 32 bits
		longBuffer.position(4);
		// Slice the buffer; gives view of the low-order 32 bits
		ByteBuffer buffer = longBuffer.slice();
		while (true) {
			buffer.clear();
			SocketAddress sa = this.channel.receive(buffer);
			if (sa == null) {
				continue; // defensive programming
			}
			// Ignore content of received datagram per RFC 868
			System.out.println("Time request from " + sa);
			buffer.clear(); // sets pos/limit correctly
			// Set 64-bit value; slice buffer sees low 32 bits 117

			longBuffer.putLong(0, (System.currentTimeMillis() / 1000)
					+ DIFF_1900);
			this.channel.send(buffer, sa);
		}
	}

	// --------------------------------------------------------------
	public static void main(String[] argv) throws Exception {
		int port = DEFAULT_TIME_PORT;
		if (argv.length > 0) {
			port = Integer.parseInt(argv[0]);
		}
		try {
			TimeServer server = new TimeServer(port);
			server.listen();
		} catch (SocketException e) {
			System.out.println("Can't bind to port " + port
					+ ", try a different one");
		}
	}
}
