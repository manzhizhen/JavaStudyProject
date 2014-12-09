/**
 * 
 */
package com.sugarmq.transport;

import javax.jms.JMSException;
import javax.jms.Message;


/**
 * 客户端传送器的接口
 * @author manzhizhen
 * 
 */
public abstract class SugarMQTransport {
	public String dispatchType; // 消息分发类型
	public int acknowledgeType; // 消息应答类型
	
	public abstract void sendMessage(Message message) throws JMSException;
	
	/**
	 * 接收消息
	 * @param time 接收超时时间，如果设置为0，表示永不超时，单位为毫秒
	 * @return
	 * @throws JMSException
	 */
	public abstract Message receiveMessage(long time) throws JMSException;

	public abstract void connect() throws JMSException;
	
	public abstract void close() throws JMSException;
	
	public abstract boolean isConnected() throws JMSException;
	
	public abstract boolean isClosed() throws JMSException;
	
	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public void setAcknowledgeType(int acknowledgeType) {
		this.acknowledgeType = acknowledgeType;
	}

	/**
	 * 返回消息分发类型
	 * @return
	 */
	public String getDispatchType() {
		return dispatchType;
	}
	
	/**
	 * 返回消息应答类型
	 * @return
	 */
	public int getAcknowledgeType() {
		return acknowledgeType;
	}
}
