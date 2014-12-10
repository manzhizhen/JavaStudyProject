/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 当磁盘上一个文件的分配空间小于它的文件大小时会出现“文件空洞”。对于内容稀疏的文
 * 件，大多数现代文件系统只为实际写入的数据分配磁盘空间（更准确地说，只为那些写入数
 * 据的文件系统页分配空间）。假如数据被写入到文件中非连续的位置上，这将导致文件出现 在逻辑上不包含数据的区域（即“空洞”）
 * 
 * @author Manzhizhen
 * 
 */
public class FileHole {
	public static void main(String[] argv) throws IOException {
		// Create a temp file, open for writing, and get
		// a FileChannel
//		File temp = File.createTempFile("holy", null);
		
		File temp = new File("testpath/temp.dat");
		if(!temp.exists()) {
			temp.createNewFile();
		}
		
		RandomAccessFile file = new RandomAccessFile(temp, "rw");
		FileChannel channel = file.getChannel();
		// Create a working buffer
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
		putData(0, byteBuffer, channel);
		putData(5000000, byteBuffer, channel);
		putData(40000, byteBuffer, channel);
		// Size will report the largest position written, but
		// there are two holes in this file. This file will
		// not consume 5 MB on disk (unless the filesystem is 74

		// extremely brain-damaged)
		System.out.println("Wrote temp file '" + temp.getPath() + "', size="
				+ channel.size());
		channel.close();
		file.close();
	}

	private static void putData(int position, ByteBuffer buffer,
			FileChannel channel) throws IOException {
		String string = "*<-- location " + position;
		buffer.clear();
		buffer.put(string.getBytes("US-ASCII"));
		buffer.flip();
		channel.position(position);
		channel.write(buffer);
	}
}
