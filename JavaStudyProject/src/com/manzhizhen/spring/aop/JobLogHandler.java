/**
 * 
 */
package com.manzhizhen.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 日志服务类
 * 代表横切逻辑，需要加入到JobService的doJob方法前后
 * @author Manzhizhen
 *
 */
public class JobLogHandler implements InvocationHandler{
	private Object target;	// 被代理的目标类
	public JobLogHandler(Object target) {
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		LogService.printLogBefore(method); // 横切逻辑
		Object object = method.invoke(target, args);
		LogService.printLogAfter(method); // 横切逻辑
		
		return object;
	}
}
