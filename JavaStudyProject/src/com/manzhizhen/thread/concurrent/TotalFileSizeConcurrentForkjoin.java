/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 使用并行方法来计算一个目录所有文件的大小（多线程） ForkJoinPool类可以根据可用的处理器数量和任务需求动态的对线程进行管理。
 * 通常我们在一个应用程序中只会使用一个fork-jion池来调度任务， 且由于该池使用了守护线程，所以用过之后也无需执行关闭操作。
 * Fork-join使用了work-stealing策略，即线程在完成自己的任务之后， 发现其他线程还有活没干完，就主动帮其他人一起干。
 * 当一个任务处于等待其他任务结束的状态时，该任务的执行线程可以将该任务挂起，然后转去执行其他任务(就像一个优秀的团队中哪些有高度责任感的成员所做的那样)。
 * 所以该方案可以完美解决TotalFileSizeNativeConcurrent中的进程死锁问题。
 * 
 * @author Manzhizhen
 * 
 */
public class TotalFileSizeConcurrentForkjoin {
	// 不需要定义线程池中线程的数量，因为ForkJoinPool会在执行任务时动态分配
	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

	private static class FileSizeFinder extends RecursiveTask<Long> {
		private static final long serialVersionUID = 1L;
		
		final File file;

		public FileSizeFinder(final File theFile) {
			file = theFile;
		}

		@Override
		public Long compute() {
			long size = 0;
			if (file.isFile()) {
				size = file.length();
			} else {
				final File[] children = file.listFiles();
				if (children != null) {
					List<ForkJoinTask<Long>> tasks = new ArrayList<ForkJoinTask<Long>>();
					for (final File child : children) {
						if (child.isFile()) {
							size += child.length();
						} else {
							tasks.add(new FileSizeFinder(child));
						}
					}
					
					// invokeAll()函数将等待所有子任务完成之后才会执行下一步循环累加操作。
					for (final ForkJoinTask<Long> task : invokeAll(tasks)) {
						size += task.join();
					}
				}
			}

			return size;
		}
	}

	public static void main(final String[] args) {
		final long start = System.nanoTime();
		final long total = forkJoinPool.invoke(new FileSizeFinder(new File(
				"C:/Windows")));
		final long end = System.nanoTime();
		System.out.println("TotalSize:" + total);
		System.out.println("Timetaken:" + (end - start) / 1.0e9);
	}
}
