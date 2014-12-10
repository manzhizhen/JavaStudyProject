package com.manzhizhen.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class LoveInputStream {
	private static final String ANSI_FILEPATH = "testpath" + File.separator + "ANSI-FILE.txt";
	private static final String UTF_8_FILEPATH = "testpath" + File.separator + "UTF-8-FILE.txt";
	
	private static final String OUTPUT_FILEPATH = "testpath" + File.separator + "OUTPUT_FILE.txt";
	
	private static final String SYSTEM_CURRENT_PATH = System.getProperty("user.dir");
	
	/**
	 * java.io.FileInputStream
	 * 读取的是格式的文件，通过FileOutputStream创建新文件 写入时也会保存为同种格式的文件
	 * @throws IOException 
	 */
	public static void testFileInputStream(String filePath) throws IOException {
		File outputFile = new File(OUTPUT_FILEPATH);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		
		try {
			byte[] bytes = new byte[512];
			if(fileInputStream != null) {
				int i = 0;
				while(fileInputStream.available() > 0) {
					i = fileInputStream.read(bytes, 0, 512);
					fileOutputStream.write(bytes, 0, i);
				}
			}
		} finally {
			fileInputStream.close();
			fileOutputStream.close();
		}
	}
	
	/**
	 * java.io.DataInputStream
	 * 抽象的类InputStream和OutputStream，这些类只在字节层次上支持读写。这意味着只能从fin对象中读取字节和字节数组。
	 * 如果想读取Java类型，就需要用到DataInputStream
	 * @throws IOException 
	 */
	public static void testDataInputStream(String filePath) throws IOException {
		File outputFile = new File(OUTPUT_FILEPATH);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(outputFile)));
		DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(
				new FileInputStream(new File(filePath))));
		
		try {
			while(dataInputStream.available() > 0) {
				dataOutputStream.writeChar(dataInputStream.readChar());
			}
			
		} finally {
			dataInputStream.close();
			dataOutputStream.close();
		}
	}
	
	/**
	 * java.io.DataInputStream
	 * 当读取输入的时候，经常需要查看下一字节是否是想要的值。
	 * Java为这种需要提供了PushbackInputStream。
	 * @throws IOException 
	 */
	public static void testPushbackInputStream(String filePath) throws IOException {
		File outputFile = new File(OUTPUT_FILEPATH);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(outputFile)));
		PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(
				new FileInputStream(new File(filePath))));
		DataInputStream dataInputStream = null;
		
		try {
			while(pushbackInputStream.available() > 0) {
				int content = pushbackInputStream.read();
				System.out.println(content);
				// 如果不是想要的，可以将它扔回去
				if(content == '1') {
					pushbackInputStream.unread(content);
					break ;
				}
			}
			
			// 如果还想读取Java类型，就要再进行一次包装
			dataInputStream = new DataInputStream(pushbackInputStream);
			// 我们将读取该文件第一个字符'1'及其后面的内容
			int readChar;
			while(dataInputStream.available() > 0) {
				// 因为char是两字节，所以当可读取的大于等于2时，才能按照字符读取，否则只能按照字节
				if(dataInputStream.available() > 1) {
					readChar = dataInputStream.readChar();
					dataOutputStream.writeChar(readChar);
				} else {
					readChar = dataInputStream.readByte();
					dataOutputStream.writeByte(readChar);
				}
				
				System.out.println(readChar);
			}
			
		} finally {
			pushbackInputStream.close();
			if(dataInputStream != null) {
				dataInputStream.close();
			}
			dataOutputStream.close();
		}
	}
	
	/**
	 * java.io.RandomAccessFile
	 * RandomAccessFile流类能够在文件的任何位置查找或者写入数据。
	 * 它同时实现了DataInput和DataOutput接口。
	 * 磁盘文件可以随机读取，但是来自网络的数据流就不行了。
	 * @throws IOException 
	 */
	public static void testRandomAccessFile(String filePath) throws IOException {
		File outputFile = new File(OUTPUT_FILEPATH);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(outputFile)));
		DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(
				new FileInputStream(new File(filePath))));
		
		try {
			while(dataInputStream.available() > 0) {
				dataOutputStream.writeChar(dataInputStream.readChar());
			}
			
		} finally {
			dataInputStream.close();
			dataOutputStream.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
//		LoveInputStream.testFileInputStream(UTF_8_FILEPATH);
//		LoveInputStream.testDataInputStream(UTF_8_FILEPATH);
		LoveInputStream.testPushbackInputStream(UTF_8_FILEPATH);
		
		System.out.println("OK!");
	}
}
