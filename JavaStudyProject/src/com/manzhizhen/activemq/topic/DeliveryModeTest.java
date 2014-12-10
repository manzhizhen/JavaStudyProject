package com.manzhizhen.activemq.topic;

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
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

/**
 * 消息的发送模式：PERSISTENT(持久性)和NON_PERSISTENT（非持久性）。
 * 前者表示消息在被消费之前，如果JMS提供者DOWN了，重新启动后消息仍然存在。后者在这种情况下表示消息会被丢失。
 * 可以通过下面的方式设置：Producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 * 
 * @author Manzhizhen
 */
public class DeliveryModeTest {
	/**
	 * 发送两种类型的消息
	 * 
	 * @throws JMSException
	 */
	private static void sendMessage() throws JMSException {
		// 注意，当系统有本地连接时才可以使用vm://localhost这个地址
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");

		TopicConnection connection = factory.createTopicConnection();
		connection.start();

		Topic topic = new ActiveMQTopic("DeliveryModeTestTopic");
		TopicSession session = connection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);

		TopicPublisher publisher = session.createPublisher(topic);

		// 设置持久模式
		publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
		publisher.send(session.createTextMessage("A persistent Message"));

		// 发送前再设置成非持久模式，注意，当发送一条消息后是无法改变该消息的持久化状态的
		publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		publisher.send(session.createTextMessage("A non persistent Message"));

		System.out.println("Send messages sucessfully!");

		session.close();
		System.out.println("session已经关闭");
		connection.close();
		System.out.println("connection已经关闭");
	}

	/**
	 * 接收消息
	 * 
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	private static void receiveMessage() throws JMSException,
			InterruptedException {
		// 注意，当系统有本地连接时才可以使用vm://localhost这个地址
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");

		TopicConnection connection = factory.createTopicConnection();
		connection.start();

		// "testQueue"参数必须和sendMessage()方法中创建的队列名字一样，否则接收不到消息
		Topic topic = new ActiveMQTopic("DeliveryModeTestTopic");
		TopicSession session = connection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);

		TopicSubscriber subscriber = session.createSubscriber(topic);
		subscriber.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("接收到的消息为："
							+ ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("Receive messages sucessfully!");
		Thread.currentThread().sleep(15000);
		session.close();
		System.out.println("session已经关闭");
		connection.close();
		System.out.println("connection已经关闭");
	}

	/**
	 * 运行方式：先调用sendMessage()方法，然后关闭退出JMS，然后再调用receiveMessage方法
	 * 
	 * @param args
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws JMSException,
			InterruptedException {
		sendMessage();
//		receiveMessage();
	}
}
