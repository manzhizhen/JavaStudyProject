/**
 * 
 */
package com.manzhizhen.activemq.queue;

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
 * JMS中，消息的传送模式有两种类型：持久性模式和非持久性模式。
 * 对于持久性模式，即使JMS提供者出现故障，该消息也不会丢失，它会在服务器恢复正常之后再次传送。
 * 对于非持久模式，如果JMS提供者出现故障，该消息可能会永久丢失。
 * @author Manzhizhen
 *
 */
public class DeliverModeTest {
	private static ActiveMQConnectionFactory factory;
	private static Connection connection;
	private static Queue queue;
	private static Session session;
	
	public static void main(String[] args) {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://169.254.131.100:61616");
			connection = factory.createConnection();
			queue = new ActiveMQQueue("DeliverModeTestQueue");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer perProducer = session.createProducer(queue);
			// 设置该发送者为持久性消息发送者 ( 默认消息也是持久化的 )
			perProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			// 如果希望消息一段时间后没有被消费就失效，可以设置TimeToLive,默认是0，
			// 表示永久有效，这里设置该消息发送者所发送的消息有效时间只有30秒。
			perProducer.setTimeToLive(30000);
			// 设置消息的优先级，0-4为普通优先级，5-9为加急优先级。默认为4
			perProducer.setPriority(9);
			
			MessageProducer noPerProducer = session.createProducer(queue);
			// 设置该发送者为非持久性消息发送者
			noPerProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			TextMessage presTextMessage = session.createTextMessage("我是持久化消息！");
			TextMessage noPresTextMessage = session.createTextMessage("我是非持久化消息！");
			
			perProducer.send(presTextMessage);
			noPerProducer.send(noPresTextMessage);
			
			// 当连接调用start()之前，该连接下的消息发送者可以将消息发送到JMS提供者，
			// 但它下面的消息接收者是无法从JMS提供者中接收到消息的。
			connection.start();
			
			System.out.println("http://169.254.131.100:8161控制台可以看到队列中有两条消息，" +
					"然后给你60秒钟去停止JMS提供者的服务（制造提供者故障）。。。");
			Thread.sleep(60000);
			
			System.out.println("给你60秒钟去开启JMS提供者的服务http://169.254.131.100:8161，控制台可以看到队列中有一条消息。。。" );
			Thread.sleep(60000);
			
			System.out.println("开始创建消息接收者。。。");
			// 创建接收者
			createConsumer();
			
			Thread.sleep(5000);
			connection.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 创建接收者
	 * 由于前面你手工停止了JMS提供者服务，然后又重新开启了JMS提供者服务，所以这里需要重新连接
	 * @throws JMSException 
	 */
	private static void createConsumer() throws JMSException {
		// 注意，即使重启过JMS提供者，我们这里也不必重新建立一个ActiveMQConnectionFactory，仅仅只需要从原factory重新创建一个连接即可。
//		factory = new ActiveMQConnectionFactory(
//				"tcp://169.254.131.100:61616");
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("接收到的消息为：" + ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		connection.start();
	}
}
