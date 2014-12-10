/**
 * 
 */
package com.manzhizhen.activemq.inaction;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Manzhizhen
 *
 */
public class BrokerFactoryBeanTest {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = new ClassPathXmlApplicationContext("com/manzhizhen/activemq/inaction/brokerFactoryBeanTest.xml");
		BrokerService brokerService = (BrokerService) applicationContext.getBean("broker");
	}
}
