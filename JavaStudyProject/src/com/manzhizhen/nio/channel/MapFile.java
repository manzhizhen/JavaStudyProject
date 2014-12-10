/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 诠释了各种模式的内存映射如何交互。具体来说，例中代码诠释了写时拷贝是如何页导
 * 向（page-oriented）的。当在使用MAP_MODE.PRIVATE模式创建的MappedByteBuffer对象上
 * 调用 put( )方法而引发更改时，就会生成一个受影响页的拷贝。这份私有的拷贝不仅反映本地更
 * 改，而且使缓冲区免受来自外部对原来页更改的影响。然而，对于被映射文件其他区域的更改还是
 * 可以看到的。 
 * @author Manzhizhen
 * 
 */
public class MapFile {
	public static void main(String[] argv) throws Exception {
		// Create a temp file and get a channel connected to it
//		File tempFile = File.createTempFile("mmaptest", null);
		File tempFile = new File("testpath/MapFile.txt");
		if(!tempFile.exists()) {
			tempFile.createNewFile();
		}
		RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
		FileChannel channel = file.getChannel();
		ByteBuffer temp = ByteBuffer.allocate(100);
		// Put something in the file, starting at location 0
		temp.put("This is the file content".getBytes());
		temp.flip();
		channel.write(temp, 0);
		// Put something else in the file, starting at location 8192.
		// 8192 is 8 KB, almost certainly a different memory/FS page.
		// This may cause a file hole, depending on the
		// filesystem page size.
		temp.clear();
		temp.put("This is more file content".getBytes());
		temp.flip();
		channel.write(temp, 8192);
		// Create three types of mappings to the same file
		MappedByteBuffer ro = channel.map(FileChannel.MapMode.READ_ONLY, 0,
				channel.size());
		MappedByteBuffer rw = channel.map(FileChannel.MapMode.READ_WRITE, 0,
				channel.size());
		MappedByteBuffer cow = channel.map(FileChannel.MapMode.PRIVATE, 0,
				channel.size());
		// the buffer states before any modifications
		System.out.println("Begin");
		showBuffers(ro, rw, cow);
		// Modify the copy-on-write buffer
		cow.position(8);
		cow.put("COW".getBytes());
		System.out.println("Change to COW buffer");
		showBuffers(ro, rw, cow);
		// Modify the read/write buffer 92

		rw.position(9);
		rw.put(" R/W ".getBytes());
		rw.position(8194);
		rw.put(" R/W ".getBytes());
		rw.force();
		System.out.println("Change to R/W buffer");
		showBuffers(ro, rw, cow);
		// Write to the file through the channel; hit both pages
		temp.clear();
		temp.put("Channel write ".getBytes());
		temp.flip();
		channel.write(temp, 0);
		temp.rewind();
		channel.write(temp, 8202);
		System.out.println("Write on channel");
		showBuffers(ro, rw, cow);
		// Modify the copy-on-write buffer again
		cow.position(8207);
		cow.put(" COW2 ".getBytes());
		System.out.println("Second change to COW buffer");
		showBuffers(ro, rw, cow);
		// Modify the read/write buffer
		rw.position(0);
		rw.put(" R/W2 ".getBytes());
		rw.position(8210);
		rw.put(" R/W2 ".getBytes());
		rw.force();
		System.out.println("Second change to R/W buffer");
		showBuffers(ro, rw, cow);
		// cleanup
		channel.close();
		file.close();
		tempFile.delete();
	}

	// Show the current content of the three buffers
	public static void showBuffers(ByteBuffer ro, ByteBuffer rw, ByteBuffer cow)
			throws Exception

	{
		dumpBuffer("R/O", ro);
		dumpBuffer("R/W", rw);
		dumpBuffer("COW", cow);
		System.out.println("");
	}

	// Dump buffer content, counting and skipping nulls
	public static void dumpBuffer(String prefix, ByteBuffer buffer)
			throws Exception {
		System.out.print(prefix + ": '");
		int nulls = 0;
		int limit = buffer.limit();
		for (int i = 0; i < limit; i++) {
			char c = (char) buffer.get(i);
			if (c == '\u0000') {
				nulls++;
				continue;
			}
			if (nulls != 0) {
				System.out.print("|[" + nulls + " nulls]|");
				nulls = 0;
			}
			System.out.print(c);
		}
		System.out.println("'");
	}
}
