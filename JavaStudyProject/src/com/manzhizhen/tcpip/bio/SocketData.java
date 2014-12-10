package com.manzhizhen.tcpip.bio;

/**
 * 客户端和服务器之间传输的数据对象
 * @author Manzhizhen
 */
public class SocketData {
	private int clientNum;	// 客户端的初始化数据
	private int serverNum;	// 被服务端处理后的数据

	public int getClientNum() {
		return clientNum;
	}

	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}
	
	@Override
	public String toString() {
		return "(" + clientNum + "~" + serverNum +")";
	}
	
	/**
	 * 将字符串转换为SocketData对象
	 * Socket是不能发送对象的，只能将对象转化成字符串发送
	 * @param socketData
	 * @return
	 */
	public String getStrFromObj() {
		return clientNum + "~" + serverNum;
	}
	
	/**
	 * 将字符串转换为SocketData对象
	 * 该方法用于把经过getStrFromObj(SocketData socketData)
	 * 的字符串转化成SocketData对象
	 * @param str
	 * @return
	 */
	public static SocketData getObjFromStr(String str) {
		SocketData socketData = new SocketData();
		String[] strs = str.trim().split("~");
		socketData.setClientNum(Integer.valueOf(strs[0]));
		socketData.setServerNum(Integer.valueOf(strs[1]));
		
		return socketData;
	}
}
