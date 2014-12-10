package com.manzhizhen.activemq.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

/**
 * 对于队列的AUTO_ACKNOWLEDGE应答模式，消费者采用监听器来接收消息是，
 * 是onMessage()方法执行之前还是执行完后告诉提供者客户端已经接收到了消息？
 * @author Manzhizhen
 *
 */
public class AutoAcknowledgeTest {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void main(String[] args) {
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
			
			// 设置消息为异步分发（默认）
			connection.setDispatchAsync(true);
			// 设置消息为同步分发
//			connection.setDispatchAsync(false);
			
			connection.start();
			Queue queue = new ActiveMQQueue("AutoAcknowledgeTest");
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			
			// 创建一个消费者
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				// 不管是同步分发还是异步分发，都是在onMessage(Message message)执行完后才给提供者确认应答
				@Override
				public void onMessage(Message message) {
					System.out.println(simpleDateFormat.format(new Date()) + " 接收到消息：" + this);
					
					try {
						// 立马给提供者消息确认应答
						// 采用CLIENT_ACKNOWLEDGE应答模式时，才可以自定义应答时间，
						// 所以，我们可以不必等onMessage(Message message)处理完毕就给予提供者消息确认
						// 需要注意的是，如果该队列只有一个消费者，即使此时应答，
						// 提供者也必须等到 onMessage(Message message)方法执行完毕才会将下一条消息发送过来
						message.acknowledge();
						
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JMSException e) {
						e.printStackTrace();
					}
					
					System.out.println(simpleDateFormat.format(new Date()) + " 处理完消息：" + this);
				}
			});
			
			
			// 创建一个生产者
			MessageProducer producer = session.createProducer(queue);
			for(int i = 0; i < 10; i++) {
				// 创建一条消息
				TextMessage msg = session.createTextMessage();
				msg.setText(simpleDateFormat.format(new Date()));
				producer.send(msg);
			}
			
			System.out.println("消息发送完毕！");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			// 控制台输入才结束该线程
			reader.readLine();
		
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
