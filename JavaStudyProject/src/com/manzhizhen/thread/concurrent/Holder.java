/**
 * 
 */
package com.manzhizhen.thread.concurrent;

/**
 * 
 * @author Manzhizhen
 *
 */
public class Holder {
	private int n;
	
	public Holder(int n) {
		this.n = n;
	}
	
	public void assertSanity() {
		int first = n;
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int second = n;
		if(first != second) {
			System.out.println("这就是由于Holder被不正确发布造成的！first:" + first + " second:" + second);
		}
	}

}
