/**
 * 
 */
package com.manzhizhen.thread.tools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Manzhizhen
 * 闭锁（CountDownLatch）用于等待事件，栅栏（CyclicBarrier）用于等待线程
 * 这是通过栅栏协调细胞自动衍生系统中的计算的例子
 */
public class CyclicBarrierTest {
	private final Board mainBoard;
	private final CyclicBarrier barrier;
	private final Worker[] workers;
	
	public CyclicBarrierTest(Board board) {
		this.mainBoard = board;
		// 向 Java 虚拟机返回可用处理器的数目
		int count = Runtime.getRuntime().availableProcessors();
		this.barrier = new CyclicBarrier(count, new Runnable() {
			@Override
			public void run() {
				mainBoard.commitNewValues();
			}
		});
	
		this.workers = new Worker[count];
		for(int i = 0; i < count; i++) {
			workers[i] = new Worker(mainBoard.getSubBoard(count, i), barrier);
		}
	}
	
	public void start() {
		for(int i = 0; i < workers.length; i++) {
			new Thread(workers[i]).start();
		}
		
		mainBoard.waitForConvergence();
	}
}


class Worker implements Runnable {
	private final Board board;
	private final CyclicBarrier barrier;
	
	public Worker(Board board, CyclicBarrier barrier) {
		this.board = board;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		while(!board.hasCoverged()) {
			for(int x = 0; x < board.getMaxX(); x++) {
				for(int y = 0; y < board.getMaxY(); y++) {
					board.setNewValue(x, y, computeValue(x, y));
				}
			}
			
			try {
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int computeValue(int x, int y) {
		int randomX = new Random(100).nextInt();
		int randomy = new Random(100).nextInt();
		return randomX * x + randomy * y;
	}
}

class Board {
	private Board parentBoard;
	
	private int maxX;
	private int maxY;
	private int value;
	
	public void commitNewValues(){
		System.out.println("最新的值为：" + value);
	}
	
	public boolean hasCoverged() {
		return value > 200;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
	public void setNewValue(int x, int y, int value) {
		this.maxX = x;
		this.maxY = y;
		this.value = value;
	}
	
	public void setParent(Board parentBoard) {
		this.parentBoard = parentBoard;
	}
	
	public Board getSubBoard(int count, int mun) {
		Board board = new Board();
		board.setParent(this);
		return board; 
	}
	
	public void waitForConvergence() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}