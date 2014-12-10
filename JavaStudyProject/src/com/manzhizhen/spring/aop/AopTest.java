/**
 * 
 */
package com.manzhizhen.spring.aop;

import java.lang.reflect.Proxy;

import org.springframework.aop.framework.ProxyFactory;

/**
 * @author Manzhizhen
 *
 */
public class AopTest {
	public static void main(String[] args) {
		/**
		 *  使用JDK提供的动态代理，只能给接口提供代理
		 */
		JobService jobService = new JobServiceImpl(); // target
		JobLogHandler jobLogHandler = new JobLogHandler(jobService);
		JobService proxyService = (JobService) Proxy.newProxyInstance(jobService.getClass().getClassLoader(), 
				jobService.getClass().getInterfaces(), jobLogHandler);
		System.out.println(proxyService.doJob());
		
		/**
		 *  使用CgLib来提供动态代理
		 */
		CgLibInterceptor proxy = new CgLibInterceptor();
		JobServiceImpl jobServiceImpl = (JobServiceImpl) proxy.getProxy(JobServiceImpl.class);
		System.out.println(jobServiceImpl.doJob());
		
		/**
		 *  使用Spring AOP
		 */
		JobService jobService1 = new JobServiceImpl();
		LogAdvice logAdvice = new LogAdvice();
		// Spring提供的代理工厂
		ProxyFactory pf = new ProxyFactory();
		// 设置代理目标
		pf.setTarget(jobService1);
		// 为代理目标添加增强，可以增加多个增强
		pf.addAdvice(logAdvice);
		// 生成代理实例
		JobService proxy1 = (JobService) pf.getProxy();
		System.out.println(proxy1.doJob());
	}
}
