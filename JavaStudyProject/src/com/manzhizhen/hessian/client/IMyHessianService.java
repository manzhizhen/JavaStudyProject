/**
 * 
 */
package com.manzhizhen.hessian.client;

import com.manzhizhen.hessian.HessianData;

/**
 * Hessian客户端接口
 * Hessian中客户端和服务端接口需要相同，但包路径可以不同
 * @author Manzhizhen
 *
 */
public interface IMyHessianService {
	public HessianData getDataObject(String id);
}
