/**
 * 
 */
package com.manzhizhen.spring.aop;

import java.lang.reflect.Method;

/**
 * 日志打印横切逻辑
 * @author Manzhizhen
 *
 */
public class LogService {
	/**
	 * 方法调用前打印
	 * @param method
	 */
	public static void printLogBefore(Method method) {
		System.out.println("准备开始调用：" + method.getName() + "()"); // 横切逻辑
	}
	
	/**
	 * 方法调用前打印
	 * @param method
	 */
	public static void printLogAfter(Method method) {
		System.out.println("方法调用完毕：" + method.getName() + "()"); // 横切逻辑
	}
}
