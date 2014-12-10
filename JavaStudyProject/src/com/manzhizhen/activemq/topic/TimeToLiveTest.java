/**
 * 
 */
package com.manzhizhen.activemq.topic;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

/**
 * 消息发送者如果想改变下一条消息的生存时间，
 * 可以在发送该消息之前setTimeToLive来设置消息的有效时间。
 * 默认为0，表示永久有效。
 * 本例我们也看看持久订阅者和普通订阅者的区别。
 * @author Manzhizhen
 *
 */
public class TimeToLiveTest {
	private static TopicSubscriber subscriber1;
	private static TopicSubscriber subscriber2;
	private static TopicSubscriber durableSubscriber1;
	private static TopicSubscriber durableSubscriber2;
	
	public static void main(String[] args) {
		// 注意，当系统有本地连接时才可以使用vm://localhost这个地址
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");

		try {
			TopicConnection connection = factory.createTopicConnection();
			// 使用持久订阅者需要给Connection设置客户端ID，必须要start开始之前设置
			connection.setClientID(InetAddress.getLocalHost().getHostName());
			connection.start();

			Topic topic = new ActiveMQTopic("DeliveryModeTestTopic");
			TopicSession session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// 创建一个主题发布者
			TopicPublisher publisher = session.createPublisher(topic);
			
			// 在消息发送前创建一个普通订阅者
			subscriber1 = session.createSubscriber(topic);
			// 正好看看普通订阅者和持久订阅者的区别，这里我们在消息发送前创建一个持久订阅者
			durableSubscriber1 = session.createDurableSubscriber(topic, "持久性订阅者1");
			// 开始监听
			// 这里需要注意，如果createListener1()方法在消息发送后再调用就“太迟了”。不过你可以试试把它放入到78行去执行。
			createListener1();
			
			// 发布一条TimeToLive为0的消息
			publisher.send(session.createTextMessage("看你收到没有，这条消息永久有效哦！"));
			// 发布一条TimeToLive为5秒的消息
			publisher.setTimeToLive(5000);
			publisher.send(session.createTextMessage("看你收到没有，这条消息5秒内你收不到的话你就再也收不到了。。"));

			System.out.println("正好让线程休息5秒，等消息过期。。。。");
			Thread.sleep(5000);
			System.out.println("至少5秒时间已经过去了。。。");
			
			// 在消息发送后创建一个普通订阅者
			subscriber2 = session.createSubscriber(topic);
			// 当消息发送后，我们再创建第二个持久订阅者，看看能否收到消息
			durableSubscriber2 = session.createDurableSubscriber(topic, "持久性订阅者2");
			// 开始监听
			createListener2();
			
			Thread.sleep(10000);
			session.close();
			connection.close();
			System.out.println("结论：持久订阅者可以收取到在它自己活跃（这里的活跃是指创建并开始监听）之前主题发布的消息（当然过期的消息是无法收取到的）。。。" +
					"这就是为什么DurableSubscriber2可以收到一条没过期的消息但非持久订阅者Subscriber2却一条消息也收取不到的原因。");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void createListener1() throws JMSException {
		subscriber1.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("Subscriber1 接收到的消息为："
							+ ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		durableSubscriber1.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("DurableSubscriber1 接收到的消息为："
							+ ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void createListener2() throws JMSException {
		subscriber2.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("Subscriber2 接收到的消息为："
							+ ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		durableSubscriber2.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println("DurableSubscriber2 接收到的消息为："
							+ ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
