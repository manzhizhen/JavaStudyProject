package com.manzhizhen.activemq.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * 本例主要用来测试哪个消费者消费消息是由提供者决定的还是客户端的运行环境决定的 结论：
 * 1.如果所有消费者都异步接收消息中，对于ActiveMQ服务器，一个队列的消息是根据客户端的Session数量来分发的
 * 然后由Session自己来分发给其下的消费者。所以这就是为什么c5消费者消费的消息占所有消息的一半。
 * 2.如果所有消费者都同步接收消息，则你可以发现及时消费者在不同的Session中创建，他们都能接收到同等数目的消息（假设他们消息处理时间相同）。
 * 3.假如一个队列只有一个消费者，这个消费者同时使用异步和同步接收消息,会抛异常，说如果已经设置了消息监听器，则无法同步接收消息。
 * @author Manzhizhen 
 * 
 */
public class ConsumerReceiveTest {
	private static ActiveMQConnectionFactory factory;
	private static Connection connection;
	private static Queue queue;
	private static Session session;
	private static Session session1;
	private static MessageConsumer cc;

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
	private static final int MESSAGE_NUM = 500;

	public static void main(String[] args) throws JMSException {
		sendMsg();
//		asynReceiveMsg();
//		synReceiveMsg();
//		oneConsumerReceive();
	}

	/**
	 * 发送消息
	 * 
	 * @throws JMSException
	 */
	public static void sendMsg() throws JMSException {
		factory = new ActiveMQConnectionFactory(
				"tcp://127.0.0.1:61616?jms.optimizeAcknowledge=true&jms.useAsyncSend=true");
		connection = factory.createConnection();
		queue = new ActiveMQQueue(
				"ConsumerReceiveTestQueue?consumer.prefetchSize=1");
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer perProducer = session.createProducer(queue);
		for (int i = 0; i < MESSAGE_NUM; i++) {
			perProducer.send(session.createTextMessage(i + 1 + ""));
		}

		System.out.println("消息发送完毕！！");
	}

	/**
	 * 异步接收消息
	 */
	public static void asynReceiveMsg() {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://127.0.0.1:61616?jms.optimizeAcknowledge=true&jms.useAsyncSend=true");
			connection = factory.createConnection();
			queue = new ActiveMQQueue(
					"ConsumerReceiveTestQueue?consumer.prefetchSize=1");
			session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			session1 = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c1 = session.createConsumer(queue);
						c1.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								try {
									String str = new SimpleDateFormat(
											DATE_FORMAT).format(new Date())
											+ " ";
									message.acknowledge();
									System.out.println(str + "c1接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c2 = session.createConsumer(queue);
						c2.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								try {
									String str = new SimpleDateFormat(
											DATE_FORMAT).format(new Date())
											+ " ";
									message.acknowledge();
									System.out.println(str + "c2接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c3 = session.createConsumer(queue);
						c3.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								try {
									String str = new SimpleDateFormat(
											DATE_FORMAT).format(new Date())
											+ " ";
									message.acknowledge();
									System.out.println(str + "c3接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c4 = session.createConsumer(queue);
						c4.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								try {
									String str = new SimpleDateFormat(
											DATE_FORMAT).format(new Date())
											+ " ";
									message.acknowledge();
									System.out.println(str + "c4接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c5 = session1.createConsumer(queue);
						c5.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								try {
									String str = new SimpleDateFormat(
											DATE_FORMAT).format(new Date())
											+ " ";
									message.acknowledge();
									System.out.println(str + "c5接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			connection.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步接收消息
	 */
	public static void synReceiveMsg() {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://127.0.0.1:61616?jms.optimizeAcknowledge=true&jms.useAsyncSend=true");
			connection = factory.createConnection();
			queue = new ActiveMQQueue(
					"ConsumerReceiveTestQueue?consumer.prefetchSize=1");
			session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			session1 = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c1 = session.createConsumer(queue);

						while (true) {
							Message message = c1.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "c1接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c2 = session.createConsumer(queue);
						while (true) {
							Message message = c2.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "c2接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c3 = session.createConsumer(queue);
						while (true) {
							Message message = c3.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "c3接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c4 = session.createConsumer(queue);
						while (true) {
							Message message = c4.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "c4接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c5 = session1.createConsumer(queue);
						while (true) {
							Message message = c5.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "c5接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			connection.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void oneConsumerReceive() {
		try {
			factory = new ActiveMQConnectionFactory(
					"tcp://127.0.0.1:61616?jms.optimizeAcknowledge=true&jms.useAsyncSend=true");
			connection = factory.createConnection();
			queue = new ActiveMQQueue(
					"ConsumerReceiveTestQueue?consumer.prefetchSize=1");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			cc = session.createConsumer(queue);

			// 同步接收消息
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						MessageConsumer c5 = session.createConsumer(queue);
						while (true) {
							Message message = c5.receive();
							try {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								message.acknowledge();
								System.out.println(str + "cc同步接收到一条消息："
										+ ((TextMessage) message).getText());
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			// 异步接收消息
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						cc.setMessageListener(new MessageListener() {
							@Override
							public void onMessage(Message message) {
								String str = new SimpleDateFormat(DATE_FORMAT)
										.format(new Date()) + " ";
								try {
									System.out.println(str + "cc异步接收到一条消息："
											+ ((TextMessage) message).getText());
									Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}).start();

			connection.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				reader.readLine();
			}

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
