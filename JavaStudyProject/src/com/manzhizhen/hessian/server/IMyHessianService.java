/**
 * 
 */
package com.manzhizhen.hessian.server;

import com.manzhizhen.hessian.HessianData;

/**
 * Hessian的服务端接口
 * Hessian中客户端和服务端接口需要相同
 * @author Manzhizhen
 *
 */
public interface IMyHessianService {
	public HessianData getDataObject(String id);
}
