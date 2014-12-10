/**
 * 
 */
package com.manzhizhen.spring.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * 日志打印的方法前增强
 * @author Manzhizhen
 *
 */
public class LogAdvice implements MethodBeforeAdvice, AfterReturningAdvice{

	// 在目标方法调用前执行
	@Override
	public void before(Method method, Object[] args, Object obj)
			throws Throwable {
		System.out.println("准备开始调用：" + method.getName() + "()"); // 横切逻辑
	}

	// 在目标方法调用后执行
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, 
			Object target) throws Throwable {
		if("finish!".equals(returnValue)) {
			System.out.println("方法调用完毕：" + method.getName() + "()"); // 横切逻辑
		} else {
			System.out.println("方法调用完毕：" + method.getName() + "(),但失败了！"); // 横切逻辑
		}
	}

}
