package com.manzhizhen.thread.concurrent;

import java.util.Random;

/**
 * 不正确的发布Holder会出现很多问题
 * @author Manzhizhen
 *
 */
public class HolderUseNotSafe {
	// 不安全的发布
	public Holder holder;
	private final Random rand = new Random(99);
	
	public void initialize() {
		holder = new Holder(Math.abs(rand.nextInt()));
	}

	public static void main(String[] args) {
		// 模拟多个线程访问通过HolderUseNotSafe来访问Holder中的assertSanity方法
		int num = 99999;
		HolderUseNotSafe holderUseNotSafe = new HolderUseNotSafe();
		Runnable[] runnables = new Runnable[num];
		for(int i = 0; i < num; i++) {
			runnables[i] = new HolderUseNotSafeThread(holderUseNotSafe);
		}
		for(int i = 0; i < num; i++) {
			new Thread(runnables[i]).start();
		}
		
		System.out.println("测试完毕!!!");
	}
}
