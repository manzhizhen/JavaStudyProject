/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * 通道提供了一种被称为Scatter/Gather的重要新功能（有时也被称为矢量I/O）。Scatter/Gather
 * 是一个简单却强大的概念，它是指在多个缓冲区上实现一个简单的I/O 操作。
 * @author Manzhizhen
 *
 */
public class ScatteringGatheringTest {
	public static void main(String[] args) throws IOException {
		// 准备测试数据
		BufferedOutputStream output = new BufferedOutputStream(
				new FileOutputStream("testpath" + File.separator + 
						"ScatteringByteChannelTest.dat"));
		output.write("5201314abcdef".getBytes());
		// 此时该文件内容总共有7*4=28byte
		output.flush();
		output.close();
		
		ScatteringByteChannel scatteringByteChannel = new FileInputStream(new File("testpath" + File.separator + 
						"ScatteringByteChannelTest.dat")).getChannel();
		
		ByteBuffer header = ByteBuffer.allocateDirect (7); 
		ByteBuffer body = ByteBuffer.allocateDirect (20); 
		ByteBuffer [] buffers = { header, body }; 
		long readBytes = scatteringByteChannel.read(buffers); // 此时前7字节放入header中，其余放入body中
		System.out.println(readBytes);
		System.out.println(header);
		System.out.println(body);
		scatteringByteChannel.close();
		
		GatheringByteChannel gatheringByteChannel = new FileOutputStream(new File("testpath" + File.separator + 
						"ScatteringByteChannelTest.dat")).getChannel();
		// 将两个Buffer组合后写进Channel中
		header.position(3);
		body.position(2);
		body.limit(5);
		// 将从header中获得4个字节和body中的3个字节写入文件中
		long writeBytes = gatheringByteChannel.write(buffers);
		System.out.println(writeBytes);
		gatheringByteChannel.close();
	}
}
 