/**
 * 
 */
package com.manzhizhen.activemq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * 消费者是如何接受消息的
 * @author Manzhizhen
 *
 */
public class ConsumerRecMsgTest {
	private static ActiveMQConnectionFactory factory;
	private static Connection connection;
	private static Queue queue;
	private static Session session1;
	private static Session session2;
	
	private static final int QUEUE_MSG_NUM = 10;
	private static final int TOPIC_MSG_NUM = 10;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.sss";
	
	public static void main(String[] args) {
//		sendQueueMsg();
		recvQueueMsg();
	}
	
	/**
	 * 向队列发送消息
	 */
	public static void sendQueueMsg() {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			connection = factory.createConnection();
				
			// 这里设置预读数为1
			queue = new ActiveMQQueue("ConsumerRecMsgTestQueue?consumer.prefetchSize=1");
			session1 = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			
			MessageProducer perProducer = session1.createProducer(queue);
			
			TextMessage message;
			for(int i = 0; i < QUEUE_MSG_NUM; i++) {
				message = session1.createTextMessage();
				message.setText(i + 1 + "");
				perProducer.send(message);
			}
			
			connection.start();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 从队列中接收消息
	 */
	public static void recvQueueMsg() {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			connection = factory.createConnection();
				
			// 这里设置预读数为1
			queue = new ActiveMQQueue("ConsumerRecMsgTestQueue?consumer.prefetchSize=5");
//			session1 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			session2 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// 采用消息监听器接收消息
//			MessageConsumer consumer1 = session1.createConsumer(queue);
			// 采用同步接收消息
			final MessageConsumer consumer2 = session2.createConsumer(queue);
			
			
/*			consumer1.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					try { 
						SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
						System.out.println(format.format(new Date()) + " consumer1 接收到消息：" + 
								((TextMessage)message).getText() + " 是否重发：" + message.getJMSRedelivered());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});*/
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Message message = null;
						while(true) {
							message = consumer2.receive();
							SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
							System.out.println(format.format(new Date()) + " consumer2 接收到消息：" + 
									((TextMessage)message).getText() + " 是否重发：" + message.getJMSRedelivered());
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			connection.start();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
	
	
}
