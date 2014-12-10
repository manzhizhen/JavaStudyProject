/**
 * 
 */
package com.manzhizhen.activemq.spring;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 发送消息的线程
 * @author Manzhizhen
 *
 */
public class SendMsgThread implements Runnable{
	private final JmsTemplate jmsTemplate;
	private final Destination sendQueue;
	private final int sendNum;
	private final int taskTime;
	
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public SendMsgThread(JmsTemplate jmsTemplate, Destination sendQueue, int sendNum, int taskTime) {
		this.jmsTemplate = jmsTemplate;
		this.sendQueue = sendQueue;
		this.sendNum = sendNum;
		this.taskTime = taskTime;
	}
	
	@Override
	public void run() {
		try {
			// 为了仿造高并发，所有线程都先休息3秒后争抢CPU使用权。
			Thread.sleep(3000);
			// 发送消息
			for(int i = 0; i < sendNum; i++) {
				jmsTemplate.send(sendQueue, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						Message message = session.createTextMessage(UUID.randomUUID().toString());
						message.setStringProperty("sendTime", simpleDateFormat.format(new Date()));
						message.setIntProperty("taskTime", taskTime);
						return message;
					}
				});
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

}
