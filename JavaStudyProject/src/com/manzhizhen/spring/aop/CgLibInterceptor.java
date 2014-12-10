/**
 * 
 */
package com.manzhizhen.spring.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 采用CgLib代理
 * @author Manzhizhen
 *
 */
public class CgLibInterceptor implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();
	
	public Object getProxy(Class<?> clazz) {
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);	
		enhancer.setCallback(this);
		
		return enhancer.create();	// 通过字节码技术动态创建子类实例
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		LogService.printLogBefore(method); 	// 横切逻辑
		Object object = methodProxy.invokeSuper(obj, args);	// 通过代理类调用父类中的方法
		LogService.printLogAfter(method); // 横切逻辑
		
		return object;
	}

}
