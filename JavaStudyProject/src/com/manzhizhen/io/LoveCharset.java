package com.manzhizhen.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.SortedMap;

public class LoveCharset {
	
	/**
	 * 输出字符集所有的别名
	 */
	public static void findCharsetAlias(String charsetName) {
		Charset cset = Charset.forName(charsetName);
		for(String alias : cset.aliases()) {
			System.out.println(alias);
		}
	}
	
	/**
	 * 输出所有可用字符集的名称
	 */
	public static void findAllCharset() {
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		for(String name : charsets.keySet()) {
			System.out.println(name);
		}
	}
	
	/**
	 * 输出此 Java 虚拟机的默认字符集
	 */
	public static void findDefaultCharset() {
		System.out.println(Charset.defaultCharset().name());
	}
	
	/**
	 * 将Unicode字符串转换为对应字符集的字节序列码
	 * @param unicodeStr
	 * @return
	 */
	public static byte[] switchUnicodeToCharset(String unicodeStr, String charsetName) {
		Charset cset = Charset.forName(charsetName);
		// 如果该字符集不支持编码（几乎所有的 charset 都支持编码。）
		if(cset == null || !cset.canEncode()) {
			return null;
		}
		
		ByteBuffer buffer = cset.encode(unicodeStr);
		return buffer.array();
	}
	
	/**
	 * 将字符集的字节序列码转换为Unicode字符串
	 * @param unicodeStr
	 * @return
	 */
	public static String switchCharsetToUnicode(byte[] bytes, String charsetName) {
		Charset cset = Charset.forName(charsetName);
		// 如果该字符集不支持编码（几乎所有的 charset 都支持编码。）
		if(cset == null || !cset.canEncode()) {
			return null;
		}
		
		// 对一个字节序列解码，需要一个字节缓冲区。使用ByteBuffer数组的静态wrap方法
		// 把一个字节数组转化为一个字节缓冲区。
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		CharBuffer cbuf = cset.decode(buffer);
		return cbuf.toString();
	}
	
	public static void main(String[] args) {
//		findCharsetAlias("UTF-8");
//		findAllCharset();
		
		String unicodeStr = "漫指针1234567890abcdefgABCDEFG";
		byte[] bytes = null;
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		for(String name : charsets.keySet()) {
			if(Charset.forName(name).canEncode()) {
				bytes = switchUnicodeToCharset(unicodeStr, name);
				System.out.print(String.format("%1$25s", name + ":" + unicodeStr.length() + " " + bytes.length + "["));
				System.out.println(switchCharsetToUnicode(bytes, name).trim() + "]");
			}
		}
	}
}
