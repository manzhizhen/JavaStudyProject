package com.sugarmq.queue;

import javax.jms.JMSException;
import javax.jms.Queue;

import com.sugarmq.message.SugarDestination;

public class SugarQueue extends SugarDestination implements Queue {
	private static final long serialVersionUID = 3731874591969734047L;
	
	private String queueName = "";
	
	public SugarQueue() {
	}
	
	public SugarQueue(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public String getQueueName() throws JMSException {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

}
