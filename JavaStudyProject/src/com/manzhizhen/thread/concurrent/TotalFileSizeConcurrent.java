/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 使用并行方法来计算一个目录所有文件的大小（多线程）
 *  我们令每个任务都返回给定目录的子目录列表和该目录下直接文件的大小而非该目录的整个大小。
 * 于是在主任务中，我们就可以分派其他任务来扫描列表中的子目录。 该方法的好处是使线程被堵住的时间不会超过扫描给定目录的直接子目录的时间。
 * 所以该方法能避免TotalFileSizeNativeConcurrent中的线程死锁问题
 * 
 * @author Manzhizhen
 */
public class TotalFileSizeConcurrent {
	/**
	 * 在扫描给定目录的子目录和文件的时候， 需要把该目录下的子目录列表和所有文件的大小返回给主线程。
	 * 所以需要SubDirectoriesAndSize内部类来保存返回值
	 * 
	 * @author Manzhizhen
	 */
	class SubDirectoriesAndSize {
		final public long size;
		final public List<File> subDirectories;

		public SubDirectoriesAndSize(final long totalSize,
				final List<File> theSubDirs) {
			size = totalSize;
			// 返回指定列表的不可修改视图。此方法允许模块为用户提供对内部列表的“只读”访问。
			subDirectories = Collections.unmodifiableList(theSubDirs);
		}
	}

	/**
	 * 返回指定目录下的之间文件大小和直接目录列表
	 * 
	 * @param file
	 * @return
	 */
	private SubDirectoriesAndSize getTotalAndSubDirs(final File file) {
		long total = 0;
		final List<File> subDirectories = new ArrayList<File>();
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			if (children != null) {
				for (final File child : children) {
					if (child.isFile())
						total += child.length();
					else
						subDirectories.add(child);
				}
			}
		}
		// 返回该目录下直接文件的大小和子目录列表
		// 因为不是返回该目录下所有文件和目录的大小，所以工作量小
		return new SubDirectoriesAndSize(total, subDirectories);
	}

	/**
	 * 具体并发逻辑交给getTotalSizeOfFilesInDir方法
	 * 相当于广度优先搜索
	 * @param file
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private long getTotalSizeOfFilesInDir(final File file)
			throws InterruptedException, ExecutionException, TimeoutException {
		// 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
		final ExecutorService service = Executors.newFixedThreadPool(100);
		try {
			long total = 0;
			final List<File> directories = new ArrayList<File>();
			directories.add(file);
			while (!directories.isEmpty()) {
				final List<Future<SubDirectoriesAndSize>> partialResults = new ArrayList<Future<SubDirectoriesAndSize>>();
				for (final File directory : directories) {
					partialResults.add(service
							.submit(new Callable<SubDirectoriesAndSize>() {
								public SubDirectoriesAndSize call() {
									return getTotalAndSubDirs(directory);
								}
							}));
				}

				directories.clear();

				for (final Future<SubDirectoriesAndSize> partialResultFuture : partialResults) {
					final SubDirectoriesAndSize subDirectoriesAndSize = partialResultFuture
							.get(100, TimeUnit.SECONDS);
					directories.addAll(subDirectoriesAndSize.subDirectories);
					total += subDirectoriesAndSize.size;
				}
			}

			return total;

		} finally {
			service.shutdown();
		}
	}

	public static void main(final String[] args) throws InterruptedException,
			ExecutionException, TimeoutException {
		final long start = System.nanoTime();
		final long total = new TotalFileSizeConcurrent()
				.getTotalSizeOfFilesInDir(new File("C:/Windows"));
		final long end = System.nanoTime();
		System.out.println("TotalSize:" + total);
		System.out.println("Timetaken:" + (end - start) / 1.0e9);
	}

}
