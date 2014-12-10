/**
 * 
 */
package com.manzhizhen.activemq.inaction;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.xbean.XBeanBrokerService;

/**
 * 如果你想通过一个初始化xml文件来启动broker，使用BrokerService可能不太合适，
 * BrokerFactory将使你可以做到这一点
 * BrokerFactory中最常使用的就是XBeanBrokerFactory
 * @author Manzhizhen
 *
 */
public class BrokerFactoryTest {
	public static void main(String[] args){
		try {
			System.setProperty("activemq.base", System.getProperty("user.dir"));
			String configUri = "xbean:com/manzhizhen/activemq/inaction/activemq-simple.xml";
			
			URI	brokerUri = new URI(configUri);
			// 如果uri是XBean风格的（即以xbean:开头的uri），
			// 则BrokerFactory#createBroker会自动调用XBeanBrokerFactory返回一个XBeanBrokerService
			BrokerService broker = BrokerFactory.createBroker(brokerUri); 
			System.out.println("broker instanceof XBeanBrokerService ：" + (broker instanceof XBeanBrokerService));
			
			broker.start();
			
			System.out.println();
			System.out.println("Press any key to stop the broker"); 
			System.out.println();
			System.in.read(); 
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
