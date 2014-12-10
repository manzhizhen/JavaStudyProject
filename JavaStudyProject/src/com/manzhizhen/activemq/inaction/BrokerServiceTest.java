/**
 * 
 */
package com.manzhizhen.activemq.inaction;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.apache.activemq.store.kahadb.KahaDBStore;

/**
 * 此例用JavaAPI来构建和启动一个broker
 * BrokerService将使你可以不用任何xml配置文件来构建和启动一个broker
 * 其结果和通过使用该包下面的activemq-simple.xml文件来启动ActiveMQ的效果是一样的
 * @author Manzhizhen
 * 
 */
public class BrokerServiceTest {
	public static void main(String[] args) throws Exception {
		BrokerService broker = new BrokerService();
		// 初始化和配置broker
		broker.setBrokerName("myBroker");
		broker.setDataDirectory("d://data/");
		
		// 设置持久化策略为KahaDB
		File dataFileDir = new File("d://data/kahadb");
		KahaDBStore kaha = new KahaDBStore(); 
		kaha.setDirectory(dataFileDir);
		broker.setPersistenceAdapter(kaha); 
		
		// 添加认证插件
		SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
		List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
		users.add(new AuthenticationUser("admin", "password",
				"admins,publishers,consumers"));
		users.add(new AuthenticationUser("publisher", "password",
				"publishers,consumers"));
		users.add(new AuthenticationUser("consumer", "password", "consumers"));
		users.add(new AuthenticationUser("guest", "password", "guests"));
		authentication.setUsers(users);
		broker.setPlugins(new BrokerPlugin[] { authentication });
		
		// 设置传输连接器
		// 注意：你必须始终保证插件在连接器之前被添加，否则插件不会被初始化，
		// 此外，任何在broker.start()后添加的连接器也无法正常启动。
		TransportConnector transportConnector = new TransportConnector();
		transportConnector.setName("openwire");
		transportConnector.setUri(new URI("tcp://localhost:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"));
		broker.addConnector(transportConnector);
//		broker.addConnector("tcp://localhost:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600");
		
		// 启动broker
		broker.start();
		
		System.out.println();
		System.out.println("Press any key to stop the broker");
		System.out.println();
		System.in.read();
	}
}
