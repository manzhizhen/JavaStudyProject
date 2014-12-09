package com.sugarmq.message.bean;

import javax.jms.JMSException;

import com.sugarmq.message.Message;

public class SugarMessage extends Message{
	private static final long serialVersionUID = 4608888574237220597L;

	public SugarMessage() {
		super();
	}
	
	
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
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException {
		// TODO Auto-generated method stub
		
	}
}
