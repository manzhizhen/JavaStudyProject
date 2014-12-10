/**
 * 
 */
package com.manzhizhen.hessian.spring;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Manzhizhen
 *
 */
public class TestThread implements Runnable{
	private ITestService testService;
	private static AtomicLong index = new AtomicLong(0);
	
	public TestThread(ITestService testService) {
		this.testService = testService;
	}
	
	@Override
	public void run() {
		long startTime = 0;
		long i = 0;
		SimpleDateFormat simplate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		String startDate = null;
		try {
			Thread.sleep(5000);
			startDate = simplate.format(new Date());
			startTime = new Date().getTime();
			i = index.incrementAndGet();
			testService.sendMsg(startTime, this.toString());
			System.out.println(i + "    " + startDate + " " + (new Date().getTime() - startTime + 0.0) / 1000 + " " + this.toString());
		} catch (Exception e) {
			System.out.println(i + "    " + startDate + " " + "发生异常耗时：" + (new Date().getTime() - startTime + 0.0) / 1000 + "  " + e.getMessage());
		}
	}

}
