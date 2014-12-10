/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用并行方法来计算一个目录所有文件的大小（多线程）
 * 我们经常需要在多个相互协作的线程之间交换数据。
 * 在TotalFileSizeConcurrentWLatch中我们使用了Future和AtomicLong来实现数据交换功能。
 * 像AtomicLong等在java.util.concurrent.atomic包中定义的其他原子操作类对于处理的单个共享数据的值来说非常拥有。
 * 为了能够同时操作多个数据值或频繁的进行的交换数据，我们有更好的方式，在本例中将体现。
 * 如果只是想在两个线程之间交换数据，我们可以使用Exchanger类。
 * 如果想要在线程间互发多组数据，则BlockingQueue接口可以派上用场。
 * @author Manzhizhen
 * 
 */
public class TotalFileSizeConcurrentWQueue {
	private ExecutorService service;
	final private BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<Long>(
			500);
	final AtomicLong pendingFileVisits = new AtomicLong();

	private void startExploreDir(final File file) {
		pendingFileVisits.incrementAndGet();
		service.execute(new Runnable() {
			public void run() {
				exploreDir(file);
			}
		});
	}

	private void exploreDir(final File file) {
		long fileSize = 0;
		if (file.isFile())
			fileSize = file.length();
		else {
			final File[] children = file.listFiles();
			if (children != null)
				for (final File child : children) {
					if (child.isFile())
						fileSize += child.length();
					else {
						startExploreDir(child);
					}
				}
		}
		try {
			fileSizes.put(fileSize);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		pendingFileVisits.decrementAndGet();
	}

	private long getTotalSizeOfFile(final String fileName)
			throws InterruptedException {
		service = Executors.newFixedThreadPool(100);
		try {
			startExploreDir(new File(fileName));
			long totalSize = 0;
			// 子线程不断扫描自己的目录并创建新的线程递归扫描，所有结果都放入fileSizes中，
			// 而主任务线程不断从fileSizes中取数据，如果fileSizes没元素了，主任务线程阻塞，
			// 如果fileSizes满了，子线程阻塞。注意，这里主任务线程和子任务扫描线程是同时工作的。 
			while (pendingFileVisits.get() > 0 || fileSizes.size() > 0) {
				final Long size = fileSizes.poll(10, TimeUnit.SECONDS);
				totalSize += size;
			}
			return totalSize;
		} finally {
			service.shutdown();
		}
	}

	public static void main(final String[] args) throws InterruptedException {
		final long start = System.nanoTime();
		final long total = new TotalFileSizeConcurrentWQueue()
				.getTotalSizeOfFile("C:/Windows");
		final long end = System.nanoTime();
		System.out.println("TotalSize:" + total);
		System.out.println("Timetaken:" + (end - start) / 1.0e9);
	}
}
