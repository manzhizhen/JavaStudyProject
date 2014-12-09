package com.sugarmq.core;


import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQTransport;

/**
 * 
 * @author manzhizhen
 *
 */
public class SugarMQConnection implements Connection{
	
	private SugarMQTransport sugarMQTransport;
	
	private Logger logger = LoggerFactory.getLogger(SugarMQConnection.class);
	
	public SugarMQConnection(SugarMQTransport sugarMQTransport) {
		this.sugarMQTransport = sugarMQTransport;
	}

	@Override
	public void close() throws JMSException {
		sugarMQTransport.close();
	}

	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination,
			String arg1, ServerSessionPool arg2, int arg3) throws JMSException {
		return null;
	}

	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic arg0,
			String arg1, String arg2, ServerSessionPool arg3, int arg4)
			throws JMSException {
		return null;
	}

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		sugarMQTransport.setAcknowledgeType(acknowledgeMode);
		SugarMQSession sugarMQSession = new SugarMQSession(transacted, sugarMQTransport);
		return sugarMQSession;
	}

	@Override
	public String getClientID() throws JMSException {
		return null;
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return null;
	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return null;
	}

	@Override
	public void setClientID(String arg0) throws JMSException {
	}

	@Override
	public void setExceptionListener(ExceptionListener arg0)
			throws JMSException {
	}

	@Override
	public void start() throws JMSException {
		sugarMQTransport.connect();
		
		long startTime = System.currentTimeMillis();
		
		while(!sugarMQTransport.isConnected()) {
			if(System.currentTimeMillis() - startTime > SugarMQConnectionFactory.OVERTIME) {
				logger.error("连接SugarMQ超时！");
				throw new JMSException("连接SugarMQ超时！");
			}
		}
	}

	@Override
	public void stop() throws JMSException {
	}

}