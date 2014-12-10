/**
 * 
 */
package com.manzhizhen.activemq.spring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Destination;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * 对DefaultMessageListenerContainer使用进行测试
 * @author Manzhizhen
 * 
 */
public class MessageListenerContainerTest {
	private static final int THREAD_NUM = 50;	// 发送消息线程数
	private final static int SEND_MSG_NUM = 8; // 每个线程发送的消息数量
	private final static int TASK_TIME = 65000; // 任务（DealAction）消耗时间（毫秒）
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static ApplicationContext applicationContext;
	
	
	public static void main(String[] args) throws ParseException {
		applicationContext = new ClassPathXmlApplicationContext("com/manzhizhen/activemq/spring/messageListenerContainerTest.xml");
//		applicationContext = new ClassPathXmlApplicationContext("classpath*:messageListenerContainerTest.xml");
		JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
		Destination sendQueue = (Destination) applicationContext.getBean("sendMsgQueue");
//		Destination rcvQueue = (Destination) applicationContext.getBean("recMsgQueue");
		
		// 创建并发线程
		Runnable[] runnables = new Runnable[THREAD_NUM];
		for(int i = 0; i < THREAD_NUM; i++) {
			runnables[i] = new SendMsgThread(jmsTemplate, sendQueue, SEND_MSG_NUM, TASK_TIME);
		}
		
		String startTime = simpleDateFormat.format(new Date());
		// 开始并发发送消息
		for(int i = 0; i < THREAD_NUM; i++) {
			new Thread(runnables[i]).start();
		}
		

	}
	
//	public static void main(String[] args) throws InterruptedException, ParseException {
//		String startTime = simpleDateFormat.format(new Date());
//		
//		Thread.sleep(3000);
//		
//		String rcvTime = simpleDateFormat.format(new Date());
//		
//		System.out.println(simpleDateFormat.parse(rcvTime).getTime() - simpleDateFormat.parse(startTime).getTime());
//	}
}
