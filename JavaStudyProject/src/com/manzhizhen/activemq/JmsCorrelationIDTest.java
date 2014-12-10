package com.manzhizhen.activemq;

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
 * JMSCorrelationID主要是用来关联多个Message，
 * 例如需要回复一个消息的时候，通常把回复的消息的JMSCorrelationID设置为原来消息的ID。
 * 
 * @author Administrator
 * 
 */
public class JmsCorrelationIDTest {
	private Queue queue;
	private Session session;

	public JmsCorrelationIDTest() throws JMSException, InterruptedException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://169.254.131.100:61616");
		Connection connection = factory.createConnection();
		connection.start();
		queue = new ActiveMQQueue("testQueue");
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		setupConsumer("ConsumerA");
		setupConsumer("ConsumerB");
		setupConsumer("ConsumerC");

		setupProducer("ProducerA", "ConsumerA");
		setupProducer("ProducerB", "ConsumerB");
		setupProducer("ProducerC", "ConsumerC");

		Thread.currentThread().sleep(20000);
		session.close();
		connection.close();
	}

	private void setupConsumer(final String name) throws JMSException {
		// 创建一个消费者，它只接受属于它自己的消息
		MessageConsumer consumer = session.createConsumer(queue, "receiver='"
				+ name + "'");
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message m) {
				try {
					MessageProducer producer = session.createProducer(queue);
					System.out.println(name + " get:"
							+ ((TextMessage) m).getText());
					// 回复一个消息
					Message replyMessage = session
							.createTextMessage("Reply from " + name);
					// 设置JMSCorrelationID为刚才收到的消息的ID
					replyMessage.setJMSCorrelationID(m.getJMSMessageID());
//					replyMessage.setStringProperty("receiver", name); 注意，不能添加这一行，因为加了这一行，consumer本身也有机会收到该消息，从而造成死循环。
					producer.send(replyMessage);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setupProducer(final String name, String consumerName)
			throws JMSException {
		MessageProducer producer = session.createProducer(queue);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// 创建一个消息，并设置一个属性receiver，为消费者的名字。
		Message message = session.createTextMessage("Message from " + name);
		message.setStringProperty("receiver", consumerName);
		producer.send(message);
		
		// 等待回复的消息
		MessageConsumer replyConsumer = session.createConsumer(queue,
				"JMSCorrelationID='" + message.getJMSMessageID() + "'");
		replyConsumer.setMessageListener(new MessageListener() {
			public void onMessage(Message m) {
				try {
					System.out.println(name + " get reply:"
							+ ((TextMessage) m).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		new JmsCorrelationIDTest();
	}
}
