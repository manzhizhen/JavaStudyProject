package com.sugarmq.message.bean;

import java.io.Serializable;
import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class SugarObjectMessage extends SugarMessage implements ObjectMessage {
	/**
	 * @param sugarMQTransport
	 */
	public SugarObjectMessage() {
		super();
	}

	private static final long serialVersionUID = -3848421894529662497L;

	@Override
	public void acknowledge() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBody() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearProperties() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable getObject() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObject(Serializable arg0) throws JMSException {
		// TODO Auto-generated method stub
		
	}
}
