/**
 * 
 */
package com.sugarmq.transport.tcp;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端消费者tcp传输器的接收应答消息线程
 * @author manzhizhen
 *
 */
public class TcpMessageTransportReceiveAcknowledgeThread implements Runnable{
	private volatile LinkedBlockingQueue<Message> receiveAcknowledgeMessageQueue = new LinkedBlockingQueue<Message>(200); // 消费者接收应答待发消息的Map
	private Socket socket;
	
	private Logger logger = LoggerFactory.getLogger(TcpMessageTransportReceiveAcknowledgeThread.class);
	
	public TcpMessageTransportReceiveAcknowledgeThread(final Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			Message message = null;
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			while(!socket.isOutputShutdown()) {
				message = receiveAcknowledgeMessageQueue.take();
				
				objectOutputStream.writeObject(message);
				objectOutputStream.flush();
				
				socket.getOutputStream().write(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.flush();
			}
		} catch (Exception e) {
			logger.error("客户端消费者应答线程异常：" + e.getMessage());
		}
	}

	public LinkedBlockingQueue<Message> getReceiveAcknowledgeMessageQueue() {
		return receiveAcknowledgeMessageQueue;
	}
}
