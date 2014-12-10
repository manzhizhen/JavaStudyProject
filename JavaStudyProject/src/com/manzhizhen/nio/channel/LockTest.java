/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

/**
 * 代码使用共享锁实现了reader进程，使用独占锁实现了writer进程。
 * 由于锁是与进程而不是Java线程关联的，您将需要运行该程序的多个拷贝。
 * 先从一个 writer和两个或更多的readers开始，
 * @author Manzhizhen
 * 
 */
public class LockTest {
	private static final int SIZEOF_INT = 4;
	private static final int INDEX_START = 0;
	private static final int INDEX_COUNT = 10;
	private static final int INDEX_SIZE = INDEX_COUNT * SIZEOF_INT;
	private ByteBuffer buffer = ByteBuffer.allocate(INDEX_SIZE);
	private IntBuffer indexBuffer = buffer.asIntBuffer();
	private Random rand = new Random();
	
	// Write new values to the index slots
	private int idxval = 1;
	private int lastLineLen = 0;

	public static void main(String[] argv) throws Exception

	{
		boolean writer = true;
		String filename = "testpath/locktest.txt";
		
		RandomAccessFile raf = new RandomAccessFile(filename, "rw");
		FileChannel fc = raf.getChannel();
		LockTest lockTest = new LockTest();
		if (writer) {
			lockTest.doUpdates(fc);
		} else {
			lockTest.doQueries(fc);
		}
	}

	// ----------------------------------------------------------------
	// Simulate a series of read-only queries while
	// holding a shared lock on the index area
	void doQueries(FileChannel fc) throws Exception {
		while (true) {
			println("trying for shared lock...");
			FileLock lock = fc.lock(INDEX_START, INDEX_SIZE, true);
			int reps = rand.nextInt(60) + 20;
			for (int i = 0; i < reps; i++) {
				int n = rand.nextInt(INDEX_COUNT);
				int position = INDEX_START + (n * SIZEOF_INT);
				buffer.clear();
				fc.read(buffer, position);
				int value = indexBuffer.get(n);
				println("Index entry " + n + "=" + value);
				// Pretend to be doing some work
				Thread.sleep(100);

			}
			lock.release();
			println("<sleeping>");
			Thread.sleep(rand.nextInt(3000) + 500);
		}
	}

	// Simulate a series of updates to the index area
	// while holding an exclusive lock
	void doUpdates(FileChannel fc) throws Exception {
		while (true) {
			println("trying for exclusive lock...");
			// 获得独占锁，该方法是阻塞方法
			FileLock lock = fc.lock(INDEX_START, INDEX_SIZE, false);
			if(lock.isShared()) {
				System.out.println("该文件系统不支持独占锁定！");
				return ;
			}
			
			// "indexBuffer" is an int view of "buffer"
			indexBuffer.clear();
			for (int i = 0; i < INDEX_COUNT; i++) {
				idxval++;
				println("Updating index " + i + "=" + idxval);
				indexBuffer.put(idxval);
				// Pretend that this is really hard work
				Thread.sleep(500);
			}
			// leaves position and limit correct for whole buffer
			buffer.clear();
			fc.write(buffer, INDEX_START);
			
			lock.release();
			
			println("<sleeping>");
			Thread.sleep(rand.nextInt(2000) + 500);
		}
	}



	// Specialized println that repaints the current line
	private void println(String msg) {
		System.out.print("\r ");
		System.out.print(msg);
		for (int i = msg.length(); i < lastLineLen; i++) {
			System.out.print(" ");
		}
		System.out.print("\r");
		System.out.flush();
		lastLineLen = msg.length();
	}
}
