/**
 * 
 */
package com.sugarmq.transport.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQTransport;

/**
 * 采用nio来传输数据
 * @author manzhizhen
 *
 */
public class NioMessageTransport extends SugarMQTransport{
	private InetSocketAddress inetSocketAddress;
	private SocketChannel socketChannel;
	private Selector selector;
	
	 private static ByteBuffer objByteBuffer = ByteBuffer.allocate(99999);
	
	private Logger logger = LoggerFactory.getLogger(NioMessageTransport.class);
	
	public NioMessageTransport() {
	}

	@Override
	public void sendMessage(Message message) throws JMSException{
		if(message == null) {
			logger.error("所发送的消息为null！！");
			return ;
		}
		
		ObjectOutputStream objectOut = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		objByteBuffer.clear();
		try {
			objectOut = new ObjectOutputStream(byteArrayOutputStream);
			objectOut.writeObject(message);
			objectOut.flush();
			
			objByteBuffer.put(byteArrayOutputStream.toByteArray());
			objByteBuffer.flip();
			socketChannel.write(objByteBuffer);
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new JMSException(e.getMessage());
		} finally {
//			try {
//				if(objectOut != null) {
//					objectOut.close();
//				}
//				
//			} catch (IOException e) {
//				logger.error(e.getMessage());
//				e.printStackTrace();
//				throw new JMSException(e.getMessage());
//			}
		}
	}
	
	@Override
	public void connect() throws JMSException {
		Selector selector;
		try {
			selector = Selector.open();
			
			socketChannel = SocketChannel.open();
			// 设置为非阻塞模式
			socketChannel.configureBlocking(false);
			
			socketChannel.connect(inetSocketAddress);
			
			// 向channel注册selector以及感兴趣的连接事件
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			logger.error("连接到提供者失败：" + inetSocketAddress.toString() + " 失败信息：" + e.getMessage());
			throw new JMSException(e.getMessage());
		}

		
	}
	
	public InetSocketAddress getInetSocketAddress() {
		return inetSocketAddress;
	}

	public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
	}

	public Selector getSelector() {
		return selector;
	}
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public void close() throws JMSException {
	}

	@Override
	public boolean isConnected() throws JMSException {
		return false;
	}

	@Override
	public boolean isClosed() throws JMSException {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.sugarmq.transport.SugarMQTransport#receiveMessage(long)
	 */
	@Override
	public Message receiveMessage(long time) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
