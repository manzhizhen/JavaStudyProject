package com.sugarmq.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 用来管理Selector及其相关的线程
 * @author manzhizhen
 *
 */
public class SugarSelectorManager {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static SugarSelectorManager sugarSelectorManager;
	
	private SugarSelectorManager(){
	}

	/**
	 * 获取一个SugarSelectorManager
	 * 单例
	 * @return
	 */
	public static SugarSelectorManager getSugarSelectorManager() {
		if(sugarSelectorManager == null) {
			sugarSelectorManager = new SugarSelectorManager();
		}
		
		return sugarSelectorManager;
	}
	
}
