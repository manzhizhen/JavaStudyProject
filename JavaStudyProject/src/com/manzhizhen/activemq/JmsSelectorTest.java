package com.manzhizhen.activemq;

import javax.jms.Connection;
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
 * 创建消息
 * @author Administrator
 *
 */
public class JmsSelectorTest {
	public static void main(String[] args) throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");

		Connection connection = factory.createConnection();
		connection.start();

		Queue queue = new ActiveMQQueue("testQueue");

		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		MessageConsumer comsumerA = session.createConsumer(queue,
				"receiver = 'A'");
		comsumerA.setMessageListener(new MessageListener() {
			public void onMessage(Message m) {
				try {
					System.out.println("Consumer A get "
							+ ((TextMessage) m).getText());
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
			}
		});

		MessageConsumer comsumerB = session.createConsumer(queue,
				"receiver = 'B'");
		comsumerB.setMessageListener(new MessageListener() {
			public void onMessage(Message m) {
				try {
					System.out.println("Consumer B get "
							+ ((TextMessage) m).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 10; i++) {
			String receiver = (i % 3 == 0 ? "A" : "B");
			TextMessage message = session.createTextMessage("Message" + i
					+ ", receiver:" + receiver);
			message.setStringProperty("receiver", receiver);
			producer.send(message);
		}
		
		Thread.currentThread().sleep(15000);
		session.close();
		connection.close();
	}
}
