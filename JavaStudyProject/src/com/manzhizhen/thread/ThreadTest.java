package com.manzhizhen.thread;

public class ThreadTest {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < 500; i++) {
			new Thread(new SystemCurrentTimeMillisThread(startTime)).start();
		}
	}
}
