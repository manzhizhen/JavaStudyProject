/**
 * 
 */
package com.manzhizhen.activemq.broker;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 利用BrokerService来启动ActiveMQ
 * @author Manzhizhen
 *
 */
public class BrokerStartupTest {
	public static void main(String[] args) {
		final CountDownLatch startupLatch = new CountDownLatch(1);
		final CountDownLatch runningLatch = new CountDownLatch(1);
		final AtomicBoolean succeed = new AtomicBoolean(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.setProperty("activemq.base", System.getProperty("user.dir"));
					System.setProperty("activemq.home", System.getProperty("user.dir"));
					System.out.println(System.getProperty("user.dir"));
					// 使用最广泛的工厂是XBeanBrokerFactory，所以这里有前缀xbean
					String configUri = "xbean:com/manzhizhen/activemq/broker/activemq.xml";
					URI brokerUri = new URI(configUri);
					BrokerService broker = BrokerFactory.createBroker(brokerUri); 
					broker.start();
					
					succeed.set(true);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					startupLatch.countDown();
				}
			}
		}).start();
		
		try {
			startupLatch.await();
			
			if(succeed.get()) {
				System.out.println("启动完毕！");
			} else {
				System.out.println("启动失败！");
				runningLatch.countDown();
			}
			
			runningLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
}
