/**
 * 
 */
package com.sugarmq.transport.tcp;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQServerTransport;

/**
 * @author manzhizhen
 *
 */
public class NioSugarMQServerTransport implements SugarMQServerTransport{
	private static Logger logger = LoggerFactory.getLogger(NioSugarMQServerTransport.class);
	
	@Override
	public void bind() throws JMSException {
	}

	@Override
	public void start() throws JMSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closed() throws JMSException {
		// TODO Auto-generated method stub
		
	}

}
