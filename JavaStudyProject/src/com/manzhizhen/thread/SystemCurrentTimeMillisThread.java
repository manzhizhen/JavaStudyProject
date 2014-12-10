/**
 * 
 */
package com.manzhizhen.thread;

/**
 * @author Manzhizhen
 *
 */
public class SystemCurrentTimeMillisThread implements Runnable{
	private long startTime;
	
	public SystemCurrentTimeMillisThread(long startTime) {
		this.startTime = startTime;
	}
	
	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			for(int i = 0; i < 50; i++) {
				System.err.println((System.currentTimeMillis() - startTime) + " " + this);
			}
		}
		
	}

}
