/**
 * 
 */
package com.manzhizhen.thread.concurrent;

/**
 * @author Manzhizhen
 *
 */
public class HolderUseNotSafeThread implements Runnable{
	private HolderUseNotSafe holderUseNotSafe;
	
	public HolderUseNotSafeThread(HolderUseNotSafe holderUseNotSafe) {
		this.holderUseNotSafe = holderUseNotSafe;
	}
	
	@Override
	public void run() {
		try {
			/**
			 * 因为它由HolderUseNotSafe中main方法的for循环调用，
			 * 由于HolderUseNotSafeThread中的run方法执行时间都非常短，
			 * 在HolderUseNotSafe#main中的for循环开始调用另一个HolderUseNotSafeThread的run方法时，
			 * 上一个HolderUseNotSafeThread的run方法早已经执行完毕，无法模拟高并发，
			 * 所以这里让线程睡眠100毫秒，然后100毫秒后这些线程争先恐后来由cpu调用，
			 * 这样才能模拟高并发
			 */
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		holderUseNotSafe.initialize();
		holderUseNotSafe.holder.assertSanity();
	}

}
