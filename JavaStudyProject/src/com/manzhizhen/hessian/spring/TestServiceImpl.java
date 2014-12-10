/**
 * 
 */
package com.manzhizhen.hessian.spring;

import java.util.Date;

/**
 * @author Manzhizhen
 *
 */
public class TestServiceImpl implements ITestService{
	@Override
	public String sendMsg(long startTime, String thread) {
		try {
			System.out.println("总耗时：" + (new Date().getTime() - startTime + 0.0) / 1000 + " 秒  " + thread);
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
