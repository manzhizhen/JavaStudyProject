/**
 * 
 */
package com.manzhizhen.spring.lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Spring容器中BeanFactory和ApplicationContext中的Bean的生命周期的测试
 * @author Manzhizhen
 *
 */
public class BeanLifecycleTest {
	public static void main(String[] args) {
		applicationContextBeanLifecycleTest();
	}
	
	/**
	 * ApplicationContext中bean生命周期测试
	 */
	public static void applicationContextBeanLifecycleTest() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:**/beanLifecycleTestTest.xml");
		BeanLifecycle beanLifecycle = (BeanLifecycle) applicationContext.getBean("beanLifecycle");
		System.out.println(beanLifecycle.getSuperMan());
		applicationContext.destroy();
	}
	
	/**
	 * ApplicationContext中bean生命周期测试
	 */
	public static void beanFactoryLifecycleTest() {
//		Resource res = new ClassPathResource("classpath:**/beanLifecycleTestTest.xml");
//		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//		BeanLifecycle beanLifecycle = (BeanLifecycle) applicationContext.getBean("beanLifecycle");
//		System.out.println(beanLifecycle.getSuperMan());
//		beanFactory.destroy();
	}
}
