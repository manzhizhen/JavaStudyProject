/**
 * 
 */
package com.manzhizhen.spring.lifecycle;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * 用来展示bean在Spring容器中的初始化过程
 * @author Manzhizhen
 * 注意，InstantiationAwareBeanPostProcessor接口继承了BeanPostProcessor接口
 */
public class BeanLifecycle implements 
	InstantiationAwareBeanPostProcessor, 
	BeanNameAware, 
	BeanFactoryAware,
	BeanPostProcessor
	{
	
	private String beanName;
	private BeanFactory beanFactory;
	
	private String superMan;
	
	public BeanLifecycle() {
		System.out.println("开始调用bean的默认构造函数");
	}
	
	public String getSuperMan() {
		return superMan;
	}

	public void setSuperMan(String superMan) {
		System.out.println("开始调用bean的setSuperMan(String superMan)");
		this.superMan = superMan;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("开始调用 BeanPostProcessor#postProcessBeforeInitialization(Object bean, String beanName) 来对bean做前期的自定义初始化");
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("开始调用 BeanPostProcessor#postProcessAfterInitialization(Object bean, String beanName) 来对bean做后期的自定义初始化");
		return bean;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName)
			throws BeansException {
		System.out.println("开始调用 InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(Object bean, String beanName) 来对bean做后期的实例化操作");
		// 正常情况下应该返回true
		return true;
	}


	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
			throws BeansException {
		System.out.println("开始调用 InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation(Class<?> beanClass, String beanName) 来对bean做前期的实例化操作");
		// 如果这里自己就完成了对bean的全部实例化操作，则直接返回实例化的对象，则Spring容器不会做后面的实例化和初始化操作。
		// 这里我们返回为null，表示让Spring容器后面继续执行默认的实例化操作
		return null;
	}


	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
			throws BeansException {
		System.out.println("开始调用 InstantiationAwareBeanPostProcessor#postProcessPropertyValues(PropertyValues pvs, " +
				"PropertyDescriptor[] pds, Object bean, String beanName) 来对bean做属性设值之前的操作");
		return pvs;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("开始调用BeanFactoryAware#setBeanFactory(BeanFactory beanFactory)");
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanName(String beanName) {
		System.out.println("开始调用BeanNameAware#setBeanName(String beanName)");
		this.beanName = beanName;
	}

}
