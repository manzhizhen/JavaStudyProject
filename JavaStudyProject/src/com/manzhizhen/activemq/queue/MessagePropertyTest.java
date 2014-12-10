/**
 * 
 */
package com.manzhizhen.activemq.queue;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
 * 消息的属性值可以是boolean、byte、short、int、long、float、double或String
 * @author Manzhizhen
 *
 */
public class MessagePropertyTest {
	public static void main(String[] args) {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");
		try {
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Session sessionForRec = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Queue queue = new ActiveMQQueue("MessagePropertyTestQueue");
			
			TextMessage textMessage = session.createTextMessage("manzhizhen");
			// 设置消息属性
			textMessage.setIntProperty("int", 1314);
			textMessage.setLongProperty("long", 1234l);
			textMessage.setDoubleProperty("double", 1.2);
			textMessage.setStringProperty("string", "I Love Y");
			Map<String, String> map = new HashMap<String, String>();
			map.put("1", "11");
			map.put("2", "22");
			textMessage.setObjectProperty("object", map);
			
			MessageProducer producer = session.createProducer(queue);
			producer.send(textMessage);
			
			MessageConsumer consumer = sessionForRec.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					// 打印出该消息携带的所有的消息属性
					try {
						Enumeration<String> enumeration = message.getPropertyNames();
						while(enumeration.hasMoreElements()) {
							System.out.println(message.getObjectProperty(enumeration.nextElement()));
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
					
				}
			});
			
			connection.start();
			System.out.println("122122222");
			Thread.sleep(10000);
			System.out.println("aaaaa");
			connection.stop();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
