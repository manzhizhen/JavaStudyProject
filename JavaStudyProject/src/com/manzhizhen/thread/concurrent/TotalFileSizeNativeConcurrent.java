/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.io.File;
import java.util.ArrayList;
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
 * 注意：该方法是为了证明造成线程死锁是多么容易，
 * 如果该目录下文件数少，还可以正常运行，
 * 一旦文件目录深，文件数量多起来，就容易造成死锁，
 * 即抛出java.util.concurrent.TimeoutException异常。
 * 
 * @author Manzhizhen
 * 
 */
public class TotalFileSizeNativeConcurrent {
	private long getTotalSizeOfFile(final String fileName)
			throws InterruptedException, ExecutionException, TimeoutException {
		final ExecutorService service = Executors.newFixedThreadPool(100);
		try {
			return getTotalSizeOfFilesInDir(service, new File(fileName));
		} finally {
			service.shutdown();
		}
	}

	private long getTotalSizeOfFilesInDir(final ExecutorService service,
			final File file) throws InterruptedException, ExecutionException,
			TimeoutException {
		if (file.isFile()) {
			return file.length();
		}

		long total = 0;
		final File[] children = file.listFiles();

		if (children != null) {
			final List<Future<Long>> partialTotalFutures = new ArrayList<Future<Long>>();
			for (final File child : children) {
				partialTotalFutures.add(service.submit(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						// 这里递归调用，
						// 当文件目录比较深，文件数量多的时候，容易造成当前进程迟迟得不到返回，即进程死锁
						return getTotalSizeOfFilesInDir(service, child);
					}
				}));
			}

			for (final Future<Long> partialTotalFuture : partialTotalFutures) {
				total += partialTotalFuture.get(100, TimeUnit.SECONDS);
			}
		}

		return total;
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException, TimeoutException {
		final long start = System.nanoTime();
		final long total = new TotalFileSizeNativeConcurrent()
				.getTotalSizeOfFile("C:/Windows");
		final long end = System.nanoTime();

		System.out.println("Total Size: " + total);
		System.out.println("Time taken:" + (end - start) / 1.0e9);
	}
}
