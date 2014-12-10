package com.manzhizhen.thread.concurrent;

import java.io.File;

/**
 * 使用串行方法来计算一个目录所有文件的大小（单线程）
 * 注意，要比较各种方法的计算目录下所有文件大小的例子时，不排除第一次计算的数据，因为第二次计算会用到系统的缓存。
 * @author Manzhizhen
 *
 */
public class TotalFileSizeSequential {
	private long getTotalsizeOfFilesInDir(final File file) {
		if(file.isFile()) {
			return file.length();
		}
		
		final File[] children = file.listFiles();
		long total = 0;
		if(children != null) {
			for(final File child : children) {
				// 递归调用
				total += getTotalsizeOfFilesInDir(child);
			}
		}
		
		return total;
	}
	
	public static void main(String[] args) {
		final long start = System.nanoTime();
		final long total = new TotalFileSizeSequential().getTotalsizeOfFilesInDir(new File("C:/Windows"));
		final long end = System.nanoTime();
		
		System.out.println("Total Size: " + total);
		System.out.println("Time taken:" + (end - start)/1.0e9);
	}
}
