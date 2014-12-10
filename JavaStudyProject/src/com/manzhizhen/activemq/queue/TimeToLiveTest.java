/**
 * 
 */
package com.manzhizhen.activemq.queue;

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
 * 可以在发送该消息之前setTimeToLive来设置消息的有效时间，
 * 默认为0，表示永久有效
 * @author Manzhizhen
 *
 */
public class TimeToLiveTest {
	public static void main(String[] args) {
		// 注意，当系统有本地连接时才可以使用vm://localhost这个地址
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");

		try {
			TopicConnection connection = factory.createTopicConnection();
			connection.start();

			Topic topic = new ActiveMQTopic("DeliveryModeTestTopic");
			TopicSession session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			TopicPublisher publisher = session.createPublisher(topic);
			
			// 发布一条TimeToLive为0的消息
			publisher.send(session.createTextMessage("看你收到没有，这条消息永久有效哦！"));
			// 发布一条TimeToLive为5秒的消息
			publisher.setTimeToLive(5000);
			publisher.send(session.createTextMessage("看你收到没有，这条消息5秒内你收不到的话你就再也收不到了。。"));

			System.out.println("正好让线程休息5秒，再创建订阅者开始接收消息。");
			Thread.sleep(5000);
			System.out.println("至少5秒时间已经过去了。。。");
			
			// 即使是无状态的消息，即使TopicSubscriber在TopicPublisher发消息之后创建，
			// 仍然可以接收到消息 ，因为默认的消息的生存时间是永久（TimeToLive默认为0），如果没有消费者，
			// JMS提供者会一直保留该消息，直到有消费者出现来消费该消息。
			TopicSubscriber subscriber = session.createDurableSubscriber(topic, "持久性订阅者1");

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

			Thread.sleep(10000);
			session.close();
			connection.close();
			System.out.println("完了。。。");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
