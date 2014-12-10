/**
 * 
 */
package com.manzhizhen.activemq.broker;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author Manzhizhen
 * 
 */
public class TopicTest2 {
	public static void main(String[] args) {
		createTopic();
	}

	private static void createTopic() {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			Queue topic = session.createQueue("TopicTestQueue1");

			MessageProducer producer = session.createProducer(topic);

			connection.start();
			for (int i = 1; i < 30; i++) {
				producer.send(session.createTextMessage(i + ""));
			}

			connection.close();

		} catch (JMSException e) {
			e.printStackTrace();
		}

		System.out.println("消息发送完毕！");
	}
}
