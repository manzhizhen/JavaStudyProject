/**
 * 
 */
package com.manzhizhen.nio.buffer;

import java.nio.CharBuffer;

/**
 * @author Manzhizhen
 *
 */
public class CharBufferTest {
	
	public static void createCharBufferTest() {
		// 通过现有的数组来创建Buffer
		char[] charArray = new char[]{'a', 'b', 'c', '1', '2', '3', '4', '5'};
		CharBuffer charBuffer1 = CharBuffer.wrap(charArray);
		CharBuffer charBuffer2 = CharBuffer.wrap(charArray, 3, 4);
		System.out.println(charBuffer1.get());
		System.out.println(charBuffer2.get());
		System.out.println(charBuffer1.capacity());
		System.out.println(charBuffer2.capacity());
		
		// 直接新建一个缓冲区，它将具有一个底层实现数组，且其数组偏移量将为零。
		CharBuffer charBuffer3 = CharBuffer.allocate(10);
		for(int i = 0; i < charBuffer3.capacity(); i++) {
			charBuffer3.put((char)('a' + i));
		}
		charBuffer3.flip();
		System.out.println(charBuffer3.toString());
		
		// 创建共享此缓冲区内容的新的字符缓冲区。通过修改该缓冲区的数据，会影响到原缓冲区，因为他们共用一个数组
		CharBuffer charBuffer11 = charBuffer1.duplicate();
		System.out.println(charBuffer11.position());
		System.out.println(charBuffer1.position());
		charBuffer11.put(4, 'W');
		System.out.println(charBuffer11);
		System.out.println(charBuffer1);
		
		// 给指定的缓冲区创建一个只读的缓冲区视图
		charBuffer3.position(4);
		CharBuffer readOnlyBuffer = charBuffer3.asReadOnlyBuffer();
		System.out.println(readOnlyBuffer.position());
		System.out.println(charBuffer3.position());
		readOnlyBuffer.position(5);
		System.out.println(readOnlyBuffer.position());
		System.out.println(charBuffer3.position());
		
		// 创建新的字节缓冲区，其内容是此缓冲区内容的共享子序列。 
		charBuffer3.position(3);
		CharBuffer subCharBuffer = charBuffer3.slice();
		System.out.println(charBuffer3.capacity());
		charBuffer3.rewind();
		System.out.println(charBuffer3);
		System.out.println(subCharBuffer.capacity());
		subCharBuffer.rewind();
		System.out.println(subCharBuffer);
		
	}
	
	public static void operCharBufferTest() {
		char[] charArray = new char[]{'a', 'b', 'c', '1', '2', '3', '4', '5'};
		CharBuffer charBuffer1 = CharBuffer.wrap(charArray);
		System.out.println(charBuffer1.get());
		// flip与rewind的区别是前者是把当前位置设置为limit，而后者不改变limit。
		System.out.println(charBuffer1.put('K').flip());
		charBuffer1.limit(charBuffer1.capacity()).rewind();
		System.out.println(charBuffer1);
	}
	
	public static void main(String[] args) {
		createCharBufferTest();
		operCharBufferTest();
	
	}
}
