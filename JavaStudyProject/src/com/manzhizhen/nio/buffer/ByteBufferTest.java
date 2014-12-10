/**
 * 
 */
package com.manzhizhen.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

/**
 * @author Manzhizhen
 *
 */
public class ByteBufferTest {
	public static void switchOtherBuffer() {
		String str1 = "abcdABCD";
		String str2 = "99999";
		
		ByteBuffer byteBuffer1 = ByteBuffer.wrap(str1.getBytes());
		// 创建直接缓冲区
		ByteBuffer directBuffer = byteBuffer1.allocateDirect(byteBuffer1.capacity());
		ByteBuffer byteBuffer2 = ByteBuffer.wrap(str2.getBytes());
		
		CharBuffer charBuffer = byteBuffer1.asCharBuffer();
		IntBuffer intBuffer = byteBuffer2.asIntBuffer();
		
		System.out.println(byteBuffer1.capacity());
		System.out.println(charBuffer.capacity());
		System.out.println(byteBuffer2.capacity());
		System.out.println(intBuffer.capacity());
		
		System.out.println(charBuffer);
		System.out.println(intBuffer.get(0));
	}
	
	
	public static void main(String[] args) {
		switchOtherBuffer();
	}
}
