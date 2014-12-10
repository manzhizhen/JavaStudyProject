/**
 * 
 */
package com.manzhizhen.activemq.inaction;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 发送消息和接收消息的一个简单例子
 * 
 * @author Manzhizhen
 * 
 */
public class RequestReplyExample implements MessageListener {
	private BrokerService broker;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;

	private final String brokerUrl = "vm://localhost";
	private final String requestQueue = "RequestQueue";

	public void start() throws Exception {
		createBroker();
		setupConsumer();
	}

	private void createBroker() throws Exception {
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);
		broker.addConnector(brokerUrl);
		broker.start();
	}

	private void setupConsumer() throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				brokerUrl);
		Connection connection;
		connection = connectionFactory.createConnection();
		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination adminQueue = session.createQueue(requestQueue);
		// 这里不设置默认的目的地，因为他需要根据消息的 JMSReplyTo属性设置。
		producer = session.createProducer(null);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		consumer = session.createConsumer(adminQueue);
		consumer.setMessageListener(this);
	}

	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		broker.stop();
	}

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage response = this.session.createTextMessage();
			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				String messageText = txtMsg.getText();
				response.setText(handleRequest(messageText));
			}
			
			response.setJMSCorrelationID(message.getJMSCorrelationID());
			producer.send(message.getJMSReplyTo(), response);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public String handleRequest(String messageText) {
		return "Response to '" + messageText + "'";
	}

	public static void main(String[] args) throws Exception {
		RequestReplyExample server = new RequestReplyExample(); 
		server.start();
		System.out.println();
		System.out.println("Press any key to stop the server"); 
		System.out.println();
		System.in.read();
		server.stop();
	}
}
