/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用并行方法来计算一个目录所有文件的大小（多线程）
 * TotalFileSizeConcurrent类中我们用到了Future类提供的两种功能，我们通过Future获取任务的执行结果，
 * 与此同时，Future也隐含的替我们完成了一些任务/线程之间的协调工作。
 * 但如果任务没有任何返回值，则Future就不太适合为线程间的协作工具了，本类就是这个例子。
 * 这里，我们递归的将扫描子目录的任务委托给不同的线程。当扫描到一个文件时，
 * 线程不再返回一个计算结果，而是去更新一个AtomicLong类型的共享变量totalSize。
 * 此外，我们还用到了叫做pendingFileVisits的AtomicLong变量，其作用是保存当前待访问的目录的数量，
 * 当该变量值变为0时，我们就调用countDown()来释放线程闩。
 * 注意：每进入一个目录我们就将pendingFileVisits加1，计算完该目录我们就将它减1，所以最终该变量为0.
 * @author Manzhizhen
 * 
 */
public class TotalFileSizeConcurrentWLatch {
	private ExecutorService service;
	final private AtomicLong pendingFileVisits = new AtomicLong();
	
	// AtomicLong提供了一个更改并取回一个简单long类型变量值的线程安全的方法。
	final private AtomicLong totalSize = new AtomicLong();

	// 一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
	final private CountDownLatch latch = new CountDownLatch(1);

	private void updateTotalSizeOfFilesInDir(final File file) {
		long fileSize = 0;
		if (file.isFile())
			fileSize = file.length();
		else {
			final File[] children = file.listFiles();
			if (children != null) {
				for (final File child : children) {
					if (child.isFile())
						fileSize += child.length();
					else {
						// 以原子方式将当前值加 1。
						pendingFileVisits.incrementAndGet();
						service.execute(new Runnable() {
							public void run() {
								updateTotalSizeOfFilesInDir(child);
							}
						});
					}
				}
			}
		}

		totalSize.addAndGet(fileSize);
		// 以原子方式将当前值减 1。
		if (pendingFileVisits.decrementAndGet() == 0)
			latch.countDown();
	}

	private long getTotalSizeOfFile(final String fileName)
			throws InterruptedException {
		service = Executors.newFixedThreadPool(100);
		pendingFileVisits.incrementAndGet();
		try {
			updateTotalSizeOfFilesInDir(new File(fileName));
			latch.await(100, TimeUnit.SECONDS);
			return totalSize.longValue();
		} finally {
			service.shutdown();
		}
	}

	public static void main(final String[] args) throws InterruptedException {
		final long start = System.nanoTime();
		final long total = new TotalFileSizeConcurrentWLatch()
				.getTotalSizeOfFile("C:/Windows");
		final long end = System.nanoTime();
		System.out.println("TotalSize:" + total);
		System.out.println("Timetaken:" + (end - start) / 1.0e9);
	}
}
