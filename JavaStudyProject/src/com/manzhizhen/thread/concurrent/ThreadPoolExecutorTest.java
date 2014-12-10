package com.manzhizhen.thread.concurrent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
	private static ThreadPoolExecutor threadPoolExecutor;
	private final static int TASK_NUM = 6;
	private static CountDownLatch countDownLatch;
	
	public static void main(String[] args) {
		countDownLatch = new CountDownLatch(TASK_NUM);
		BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(5);
		threadPoolExecutor = new ThreadPoolExecutor(10, 30, 30L,
				TimeUnit.SECONDS, taskQueue);

		executorTasks(TASK_NUM);

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(threadPoolExecutor);
		
		try {
			Thread.sleep(32000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(threadPoolExecutor);
		
		threadPoolExecutor.shutdown();
	}

	private static Collection<Runnable> getNewTaskThread(int num) {
		Collection<Runnable> taskThreads = new ArrayList<Runnable>(num);
		for (int i = 0; i < num; i++) {
			taskThreads.add(new TaskThread(threadPoolExecutor, countDownLatch, 
					"Thread" + (i + 1)));
		}
		
		return taskThreads;
	}

	/**
	 * 返回指定的任务数量
	 * 
	 * @param num
	 * @return
	 */
	private static void executorTasks(int num) {
		Collection<Runnable> tasks = getNewTaskThread(num);
		Iterator<Runnable> iterator = tasks.iterator();
		while (iterator.hasNext()) {
			threadPoolExecutor.execute(iterator.next());
		}
	}

	/**
	 * 任务线程
	 * 
	 * @author 625288
	 */
	static class TaskThread implements Runnable {
		private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.sss";
		private ThreadPoolExecutor threadPoolExecutor;
		private CountDownLatch countDownLatch;
		private String name;

		public TaskThread(ThreadPoolExecutor threadPoolExecutor, CountDownLatch countDownLatch, 
				String name) {
			this.threadPoolExecutor = threadPoolExecutor;
			this.countDownLatch = countDownLatch;
			this.name = name + " create-time:"
					+ new SimpleDateFormat(DATE_FORMAT).format(new Date());
		}

		@Override
		public void run() {
			try {
				System.out.println(name + " " + threadPoolExecutor);
				Thread.sleep(10000l);
				countDownLatch.countDown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
