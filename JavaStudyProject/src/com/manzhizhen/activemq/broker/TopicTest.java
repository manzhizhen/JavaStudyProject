/**
 * 
 */
package com.manzhizhen.activemq.broker;

import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author Manzhizhen
 *
 */
public class TopicTest {
	public static void main(String[] args) {
		createTopic();
	}
	
	private static void createTopic() {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61617");
			ConnectionFactory factory1 = new ActiveMQConnectionFactory("tcp://localhost:61618");
			Connection connection1 = factory1.createConnection();
			Connection connection = factory.createConnection();
			connection.setClientID("我爱你");
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Session session1 = connection1.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue topic = session.createQueue("TopicTestQueue1");
			Queue topic1 = session1.createQueue("TopicTestQueue1");
			final MessageConsumer consumer = session.createConsumer(topic);
//			final MessageConsumer durableConsumer = session.createDurableSubscriber(topic, "我是持久化订阅者");
//			final MessageConsumer durableConsumer1 = session.createDurableSubscriber(topic, "我是持久化订阅者2");
//			final MessageConsumer durableConsumer = session.createConsumer(topic);
//			final MessageConsumer durableConsumer1 = session.createConsumer(topic);
			
			final MessageConsumer durableConsumer2 = session1.createConsumer(topic1);
			final MessageConsumer durableConsumer3 = session1.createConsumer(topic1);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							System.out.println("consumer 接收到：" + ((TextMessage)consumer.receive()).getText());
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							System.out.println("durableConsumer2 接收到：" + ((TextMessage)durableConsumer2.receive()).getText());
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							System.out.println("durableConsumer3 接收到：" + ((TextMessage)durableConsumer3.receive()).getText());
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			
			connection.start();
			connection1.start();
			
			System.out.println("消费者创建完毕！");
			
			CountDownLatch latch = new CountDownLatch(1);
			latch.await();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("消费者程序结束！");
	}
}
