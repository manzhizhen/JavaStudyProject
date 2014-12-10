/**
 * 
 */
package com.manzhizhen.spring.aop;

/**
 * 服务接口
 * 此例用来演示如何使用JDK的动态代理技术
 * 
 * @author Manzhizhen
 * @since jdk 1.3
 */
public interface JobService {
	/**
	 * 做一件事情
	 * @return
	 */
	public String doJob();
}
