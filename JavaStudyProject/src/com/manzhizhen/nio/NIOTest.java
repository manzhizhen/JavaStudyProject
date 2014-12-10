package com.manzhizhen.nio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

public class NIOTest {
	/**
	 * 不带缓冲的直接CRC32循环冗余校验方式
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static long checksumInputStream(String fileName) throws IOException {
		InputStream in = new FileInputStream(fileName);
		CRC32 crc = new CRC32();
		
		int c;
		while((c = in.read()) != -1) {
			crc.update(c);
		}
		
		return crc.getValue();
	}
	
	/**
	 * 带缓冲的直接CRC32循环冗余校验方式
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static long checksumBufferedInputStream(String fileName) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(fileName));
		CRC32 crc = new CRC32();
		
		int c;
		while((c = in.read()) != -1) {
			crc.update(c);
		}
		
		return crc.getValue();
	}
	
	/**
	 * 使用随机访问来进行CRC32循环冗余校验方式
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static long checksumRandomAccessInputStream(String fileName) throws IOException {
		RandomAccessFile file = new RandomAccessFile(fileName, "r");
		long length = file.length();
		CRC32 crc = new CRC32();
		
		for(long p = 0; p < length; p++) {
			file.seek(p);
			int c = file.readByte();
			crc.update(c);
		}
		
		return crc.getValue();
	}
	
	/**
	 * 使用内存映射进行CRC32循环冗余校验方式
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static long checksumMappedFile(String fileName) throws IOException {
		FileInputStream in = new FileInputStream(fileName);
		FileChannel channel = in.getChannel();
		
		CRC32 crc = new CRC32();
		int length = (int)channel.size();
		MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
		
		for(int p = 0; p < length; p++) {
			int c = buffer.get(p);
			crc.update(c);
		}
		
		return crc.getValue();
	}
	
	public static void main(String[] args) throws IOException {
		String filePath = "C:/Users/Manzhizhen/AppData/Local/MyEclipse/Common/" +
				"binary/com.sun.java.jdk.win32.x86_1.6.0.013/src.zip";
		
		System.out.println("比拼开始了：");
		
		long start = System.currentTimeMillis();
		long crcValue = checksumInputStream(filePath);
		long end = System.currentTimeMillis();
		System.out.println("checksumInputStream:" + (end - start) + " CRC:" + crcValue);
		
		start = System.currentTimeMillis();
		crcValue = checksumBufferedInputStream(filePath);
		end = System.currentTimeMillis();
		System.out.println("checksumBufferedInputStream:" + (end - start) + " CRC:" + crcValue);
		
		start = System.currentTimeMillis();
		crcValue = checksumRandomAccessInputStream(filePath);
		end = System.currentTimeMillis();
		System.out.println("checksumRandomAccessInputStream:" + (end - start) + " CRC:" + crcValue);
		
		start = System.currentTimeMillis();
		crcValue = checksumMappedFile(filePath);
		end = System.currentTimeMillis();
		System.out.println("checksumMappedFile:" + (end - start) + " CRC:" + crcValue);
		
	}
}












