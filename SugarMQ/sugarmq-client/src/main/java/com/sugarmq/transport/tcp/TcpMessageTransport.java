/**
 * 
 */
package com.sugarmq.transport.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.MessageDispatcher;
import com.sugarmq.transport.SugarMQTransport;
import com.sugarmq.util.MessageIdGenerate;

/**
 * 采用Socket来传输对象
 * @author manzhizhen
 *
 */
public class TcpMessageTransport extends SugarMQTransport{
	private InetAddress inetAddress;
	private int port;
	private Socket socket;
	
	 private static ByteBuffer objByteBuffer = ByteBuffer.allocate(2048);
	
	private TcpMessageTransportSendThread tcpMessageTransportSendThread;	// 生产者发送消息的线程
	private TcpMessageTransportSendAcknowledgeThread tcpMessageTransportSendAcknowledgeThread;	// 生产者发送消息接收应答的线程
	private TcpMessageTransportReceiveThread tcpMessageTransportReceiveThread; // 消费者消息接收线程
	private TcpMessageTransportReceiveAcknowledgeThread tcpMessageTransportReceiveAcknowledgeThread; // 消费者消息接收应答线程
	
	private MessageDispatcher messageDispatch;	// 消息分发器
	
	private Logger logger = LoggerFactory.getLogger(TcpMessageTransport.class);
	
	public TcpMessageTransport(String dispatchType, int acknowledgeType) {
		this.dispatchType = dispatchType;
		this.acknowledgeType = acknowledgeType;
		messageDispatch = new MessageDispatcher(this);
	}

	@Override
	public void sendMessage(Message message) throws JMSException{
		if(message == null) {
			logger.error("所发送的消息为null！！");
			return ;
		}
		
		if(!socket.isConnected() || socket.isOutputShutdown()) {
			throw new JMSException("Socket状态不正常，无法发送消息！");
		}
		
		try {
			Semaphore semaphore = new Semaphore(1);
			semaphore.acquire();
			
			// 这里需要在客户端临时生成一个messageId来做为消息应答的标记
			message.setJMSMessageID(MessageIdGenerate.getNewMessageId());
			tcpMessageTransportSendAcknowledgeThread.getSendAcknowledgeMessageMap().put(message.getJMSMessageID(), semaphore);
			tcpMessageTransportSendThread.getSendMessageQueue().put(message);
			
			semaphore.acquire();
			System.out.println("接收到消息应答：" + message.getJMSMessageID());
		} catch (Exception e) {
			logger.error("发送消息失败：" +  e.getMessage());
			throw new JMSException("发送消息失败：" +  e.getMessage());
		}
	}
	
	@Override
	public void connect() throws JMSException {
		try {
			socket = new Socket(inetAddress, port);
			
			// 创建消息接收应答线程
			tcpMessageTransportSendAcknowledgeThread = new TcpMessageTransportSendAcknowledgeThread(socket);
			new Thread(tcpMessageTransportSendAcknowledgeThread).start();	
			
			// 创建消息发送线程
			tcpMessageTransportSendThread = new TcpMessageTransportSendThread(socket);
			new Thread(tcpMessageTransportSendThread).start();
			
			// 创建消费者消息接收应答线程
			tcpMessageTransportReceiveAcknowledgeThread = new TcpMessageTransportReceiveAcknowledgeThread(socket);
			new Thread(tcpMessageTransportReceiveAcknowledgeThread).start();
			
			// 创建消费者消息接收线程
			tcpMessageTransportReceiveThread = new TcpMessageTransportReceiveThread(socket, messageDispatch, 
					tcpMessageTransportReceiveAcknowledgeThread.getReceiveAcknowledgeMessageQueue());
			new Thread(tcpMessageTransportReceiveThread).start();
			
			
		} catch (IOException e) {
			logger.error("连接到SugarMQ失败：" + socket.toString() + " 失败信息：" + e.getMessage());
			throw new JMSException(e.getMessage());
		}

		
	}
	
	@Override
	public void close() throws JMSException {
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("关闭Socket出错：", e.getMessage());
				throw new JMSException("关闭Socket出错：", e.getMessage());
			}
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public boolean isConnected() throws JMSException {
		return socket == null ? false : socket.isConnected();
	}

	@Override
	public boolean isClosed() throws JMSException {
		return socket == null ? false : socket.isClosed();
	}

	@Override
	public Message receiveMessage(long time) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDispatchType() {
		return dispatchType;
	}

	@Override
	public int getAcknowledgeType() {
		return acknowledgeType;
	}

}
