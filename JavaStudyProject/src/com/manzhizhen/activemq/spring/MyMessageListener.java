/**
 * 
 */
package com.manzhizhen.activemq.spring;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

/**
 * 处理消息的监听器
 * @author Manzhizhen
 *
 */
@Component("myMessageListener")
public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
//			new DealAction((TextMessage) message).execute();
			System.out.println(Thread.currentThread());
//			System.out.println("执行完毕！");
		}
	}

}
