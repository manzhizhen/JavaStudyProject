/**
 * 
 */
package com.manzhizhen.spring.aop;

/**
 * 服务实现类
 * 此例用来演示如何使用JDK的动态代理技术
 * 
 * @author Manzhizhen
 *
 */
public class JobServiceImpl implements JobService {

	@Override
	public String doJob() {
		try {
			Thread.sleep(2000);
			System.out.println("Hello, I Love U!");
			return "finish!";
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "fail";
	}

}
