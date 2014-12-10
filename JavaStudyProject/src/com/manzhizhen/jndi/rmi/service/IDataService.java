/**
 * 
 */
package com.manzhizhen.jndi.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI要求服务提供者和服务使用者接口都要继承与Remote
 * 并且所有远程调用方法都需要抛出RemoteException异常
 * 调用接口IDataService的包名需要和客户端的包名一致
 * @author Manzhizhen
 *
 */
public interface IDataService extends Remote, Serializable{
	/**
	 * 获取当前系统日期的字符串形式
	 * @return
	 */
	public String getSysCurrentDateStr() throws RemoteException;
	
	/**
	 * 根据名称获取数据对象
	 * @return RmiTestData
	 */
	public RmiTestData getDataObject(String name) throws RemoteException;
}
