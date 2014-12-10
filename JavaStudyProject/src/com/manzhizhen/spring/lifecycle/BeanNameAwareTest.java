/**
 * 
 */
package com.manzhizhen.spring.lifecycle;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 当bean被初始化和设置依赖完成之后，在getBean返回bean之前，
 * 如果该bean实现了BeanNameAware接口，Spring容器会为其设置该bean在Spring容器中的id
 * @author Manzhizhen
 *
 */
public class BeanNameAwareTest implements BeanNameAware{
	private String beanName;

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanName()	{
		return beanName;
	}
	
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:**/beanNameAwareTest.xml");
		BeanNameAwareTest springTest = (BeanNameAwareTest) appContext.getBean("springTest");
		System.out.println(springTest.getBeanName());
	}
}
