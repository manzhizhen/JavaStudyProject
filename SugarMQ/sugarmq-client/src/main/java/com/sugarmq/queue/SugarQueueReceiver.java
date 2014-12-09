package com.sugarmq.queue;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQTransport;

public class SugarQueueReceiver implements QueueReceiver {
	private SugarMQTransport sugarMQTransport;
	private String messageSelector;
	private Destination destination;
	
	private MessageListener messageListener;
	
	private Logger logger = LoggerFactory.getLogger(SugarQueueReceiver.class);
	
	public SugarQueueReceiver(SugarMQTransport sugarMQTransport, Destination destination) throws JMSException{
		if(sugarMQTransport == null) {
			throw new JMSException("创建队列消费者失败，SugarMQTransport为空！");
		}
		
		if(destination == null) {
			throw new JMSException("创建队列消费者失败，Destination为空！");
		}
		
		this.sugarMQTransport = sugarMQTransport;
		this.destination = destination;
	}
	

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		return messageListener;
	}

	@Override
	public String getMessageSelector() throws JMSException {
		return messageSelector;
	}

	@Override
	public Message receive() throws JMSException {
		return receive(0);
	}

	@Override
	public Message receive(long time) throws JMSException {
		return sugarMQTransport.receiveMessage(time);
	}

	@Override
	public Message receiveNoWait() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageListener(MessageListener messageListener) throws JMSException {
		if(messageListener == null) {
			throw new JMSException("消息监听器不能为空！");
		}
		
		this.messageListener = messageListener;
	}

	@Override
	public Queue getQueue() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
