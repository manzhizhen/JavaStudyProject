package com.sugarmq.queue;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

public class SugarQueueBrowser implements QueueBrowser {
	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Enumeration getEnumeration() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessageSelector() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Queue getQueue() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
