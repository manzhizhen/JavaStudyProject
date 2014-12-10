/**
 * 
 */
package com.manzhizhen.hessian.server;

import java.util.HashMap;
import java.util.Map;

import com.manzhizhen.hessian.HessianData;


/**
 * Hessian的服务端实现类
 * Hessian必须通过Servlet来提供远程服务，
 * 所以服务端的所有代码需要放置在JavaStudyWebProject这个Web项目中
 * @author Manzhizhen
 */
public class MyHessianServiceImpl implements IMyHessianService {
	private Map<String, HessianData> dataMap;
	
	public MyHessianServiceImpl() {
		dataMap = new HashMap<String, HessianData>(2);
		dataMap.put("01", new HessianData("01", "manzhizhen", 20));
		dataMap.put("02", new HessianData("02", "lover", 18));
	}

	@Override
	public HessianData getDataObject(String id) {
		return dataMap.get(id);
	}

}
