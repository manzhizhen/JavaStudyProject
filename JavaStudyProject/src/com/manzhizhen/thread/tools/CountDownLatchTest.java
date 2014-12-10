/**
 * 
 */
package com.manzhizhen.thread.tools;

import java.util.concurrent.CountDownLatch;

/**
 * 演示如果使用闭锁(CountDownLatch)
 * 为什么要在CountDownLatchTest使用闭锁，而不是在线程创建后就立即启动？
 * 或许我们希望测试并发执行某个任务需要的时间，如果在创建线程后立即启动它们，
 * 那么先启动的线程将“领先”后启动的线程，并且活跃线程数量会随着时间的对弈而增加或减少，
 * 竞争程度也在不断发生变化。启动门（startGate）将是的主线程能够同时释放所有工作线程，
 * 而结束门（endGate）则使主线程能够等待最后一个线程执行完成，而不是顺序的等待每个线程执行完成。
 * @author Manzhizhen
 *
 */
public class CountDownLatchTest {
	public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException{
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThreads);
		
		for(int i = 0; i < nThreads; i++) {
			Thread t = new Thread() {
				public void run() {
					try {
						
						startGate.await(); // 只有主线程对startGate countDown后才会让该子线程通过
						try {
							task.run();
						} finally {
							endGate.countDown();
						}
						
					} catch(InterruptedException e) {
					}
				}
			};
			
			t.start();
		}
		
		long start = System.nanoTime();
		startGate.countDown();
		endGate.await();	// 该方法将等待所有线程执行完毕才让主线程通过
		long end = System.nanoTime();
		
		return end - start;
	}
	
	public static void main(String[] args) {
		
	}
}

class TaskTest implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
