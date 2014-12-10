package com.manzhizhen.tcpip.nio;

import com.manzhizhen.tcpip.bio.SocketData;

/**
 * 用户客户端和服务端的沟通对象
 * 
 * @author Manzhizhen
 * 
 */
public class ChannelData {
	private String userName; // 用户姓名
	private String password; // 用户密码
	private int level = 0; // 用户等级

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "用户姓名:" + userName + "用户密码:" + password + " 用户等级:"
				+ (level == 0 ? "无等级" : level);
	}
	
	/**
	 * 将字符串转换为ChannelData对象
	 * Socket是不能发送对象的，只能将对象转化成字符串发送
	 * @param socketData
	 * @return
	 */
	public String getStrFromObj() {
		return userName + "~" + password + "~" + level;
	}
	
	/**
	 * 将字符串转换为ChannelData对象
	 * 该方法用于把经过getStrFromObj(ChannelData channelData)
	 * 的字符串转化成ChannelData对象
	 * @param str
	 * @return
	 */
	public static ChannelData getObjFromStr(String str) {
		ChannelData channelData = new ChannelData();
		String[] strs = str.trim().split("~");
		channelData.setUserName(strs[0]);
		channelData.setPassword(strs[1]);
		channelData.setLevel(Integer.valueOf(strs[2]));
		return channelData;
	}

}
