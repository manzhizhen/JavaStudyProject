/**
 * 
 */
package com.manzhizhen.activemq.spring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * 业务处理Action
 * @author Manzhizhen
 *
 */
public class DealAction {
	private final TextMessage message;
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final Object lock = new Object();
	private static long smallTime = 100;
	private static long maxTime = 0;
	private static long totalTime = 0;
	
	public DealAction(final TextMessage message) {
		this.message = message;
	}
	
	public void execute() {
		try {
			String rcvTime = simpleDateFormat.format(new Date());
			String sendTime = message.getStringProperty("sendTime");
			long allTime = simpleDateFormat.parse(rcvTime).getTime() - simpleDateFormat.parse(sendTime).getTime();
			
			synchronized (lock) {
				if(allTime < smallTime) {
					smallTime = allTime;
				}
				
				if(allTime > maxTime) {
					maxTime = allTime ;
				}
				
				totalTime += allTime;
			}
			
//			System.err.println("消息发送时间:" + sendTime + " 消息接收时间：" + rcvTime
//					+ " 时间间隔：" + allTime 
//					+ " 当前线程：" + Thread.currentThread().toString());
			
			System.err.println("smallTime:" + smallTime + " maxTime:" + maxTime + " totalTime:" + totalTime);
			
//			 假设业务处理耗时为3秒
			Thread.sleep(message.getIntProperty("taskTime"));
			
//			System.err.println(simpleDateFormat.format(new Date()) + " 消息:" + message.getText() + " 业务处理完毕！！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		
		}
		catch (JMSException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
