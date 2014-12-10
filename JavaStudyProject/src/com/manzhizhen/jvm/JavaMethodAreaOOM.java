/**
 * 
 */
package com.manzhizhen.jvm;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 用CGLib产生大量的类使JVM方法区溢出
 * @author Manzhizhen
 *
 */
public class JavaMethodAreaOOM {
	public static void main(String[] args) {
		while(true) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(OOMObject.class);
			enhancer.setUseCache(false);
			enhancer.setCallback(new MethodInterceptor() {
				
				@Override
				public Object intercept(Object obj, Method arg1, Object[] args,
						MethodProxy proxy) throws Throwable {
					return proxy.invoke(obj, args);
				}
			});
			
		}
	}
	
	static class OOMObject{
	}
}
