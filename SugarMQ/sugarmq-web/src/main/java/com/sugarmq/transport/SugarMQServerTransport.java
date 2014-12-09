/**
 * 
 */
package com.sugarmq.transport;

import javax.jms.JMSException;

/**
 * 服务端传送器接口
 * @author manzhizhen
 *
 */
public interface SugarMQServerTransport {
	public void bind() throws JMSException;
	public void start() throws JMSException;
	public void closed() throws JMSException;
}
