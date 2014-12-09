/**
 * 
 */
package com.sugarmq.transport.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端tcp传输器的发送消息线程
 * 
 * @author manzhizhen
 * 
 */
public class TcpMessageTransportSendThread implements Runnable {
	private volatile LinkedBlockingQueue<Message> sendMessageQueue = 
			new LinkedBlockingQueue<Message>(200); // 待发送的消息队列
	private Socket socket;
	
	private Logger logger = LoggerFactory
			.getLogger(TcpMessageTransportSendThread.class);

	public TcpMessageTransportSendThread(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			
			Message message = null;
			while (!socket.isOutputShutdown()) {
				message = sendMessageQueue.take();
				logger.info("从队列中获取到了一个消息：{}", message);
				objectOutputStream.writeObject(message);
				objectOutputStream.flush();
				
				socket.getOutputStream().write(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.flush();
				logger.info("消息发送完毕：{}", message);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			
			if(objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	public LinkedBlockingQueue<Message> getSendMessageQueue() {
		return sendMessageQueue;
	}

}
