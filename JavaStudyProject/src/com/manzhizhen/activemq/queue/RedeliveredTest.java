/**
 * 
 */
package com.manzhizhen.activemq.queue;

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
 * 本例主要用来测试ActiveMQ重发次数到底是多少
 * 重发次数也许和预读数PrefetchSize有关
 * @author Manzhizhen
 *
 */
public class RedeliveredTest {
	private static ActiveMQConnectionFactory factory;
	private static Connection connection;
	private static Queue queue;
	private static Session session;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.sss";
	
	public static void main(String[] args) {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			connection = factory.createConnection();
				
			// 这里设置预读数为100
			queue = new ActiveMQQueue("RedeliveredTestQueue?consumer.prefetchSize=100");
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			
			MessageProducer perProducer = session.createProducer(queue);
			
			// 总共发送一百条消息
			int messageTotalNum = 10;
			// 这种测试不需要消息持久化
			perProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage message;
			for(int i = 0; i < messageTotalNum; i++) {
				message = session.createTextMessage();
				message.setText(i + 1 + "");
				perProducer.send(message);
			}
			
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					try { 
						SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
						System.out.println(format.format(new Date()) + " 接收到消息：" + 
								((TextMessage)message).getText() + " 是否重发：" + message.getJMSRedelivered());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			connection.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				reader.readLine();
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
