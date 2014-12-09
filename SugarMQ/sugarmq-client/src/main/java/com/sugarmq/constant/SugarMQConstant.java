/**
 * 
 */
package com.sugarmq.constant;

/**
 * @author manzhizhen
 *
 */
public class SugarMQConstant {
	/**
	 * 连接器类型
	 * 目前只支持TCP和NIO
	 * @author manzhizhen
	 */
	/*public enum TransportType {
		TRANSPORT_TCP("tcp", "TCP"),
		TRANSPORT_NIO("nio", "NIO");
		
		String key;
		String value;
		TransportType(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return key;
		}
	}*/
	
	/**
	 * 提供者用到的消息属性
	 * @author manzhizhen
	 *
	 */
	public enum MessageProperty {
		DISABLE_MESSAGE_ID("#disableMessageId", false),
		SESSION_ID("#sessionId", null);
		
		String key;
		Object value;
		private MessageProperty(String key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return key;
		}
	}
	
	/**
	 * 消息分发类型
	 */
	public enum MessageDispatchType {
		IN_TURN("IN_TURN");
		
		String value;
		private MessageDispatchType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	/**
	 * 消息类型
	 */
	public enum MessageType {
		PRODUCER_MESSAGE("PRODUCER_MESSAGE"),	// 生产者发送的消息
		CUSTOMER_ACKNOWLEDGE_MESSAGE("CUSTOMER_ACKNOWLEDGE_MESSAGE");	// 消费者应答消息
		
		String value;
		private MessageType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
}
