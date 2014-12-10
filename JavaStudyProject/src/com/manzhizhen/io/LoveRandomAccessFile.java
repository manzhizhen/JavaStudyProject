package com.manzhizhen.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class LoveRandomAccessFile {
	private static final String UTF_8_FILEPATH = "testpath" + File.separator + "UTF-8-FILE.txt";

	/**
	 * 用RandomAccessFile在文件每行开头加上数字行号
	 * RandomAccessFile流类能够在文件的任何位置查找或者写入数据。
	 * @param filePath
	 * @throws IOException 
	 */
	public static void addRowNumAtRowHead(String filePath) throws IOException {
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()) {
			file.createNewFile();
		}
		
		String anscii = Charset.forName("iso-8859-1").name();
		String utf8 = Charset.forName("utf8").name();
		
		RandomAccessFile inOut = new RandomAccessFile(file.getAbsolutePath(), "rw");
		// 不知道为什么有乱码问题
		for(long rowNum = 1; inOut.getFilePointer() < inOut.length(); rowNum++) {
			String str = new String((rowNum + " ").getBytes(anscii), utf8);
			inOut.writeUTF(str);
//			inOut.writeChar();
			System.out.println(new String(inOut.readLine().getBytes(anscii), utf8));
		}
		
		
		inOut.close();
	}
	
	public static void main(String[] args) throws IOException {
		LoveRandomAccessFile.addRowNumAtRowHead(UTF_8_FILEPATH);
		System.out.println("over");
	}
	
}
