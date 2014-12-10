/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Manzhizhen
 * 
 */
public class ChannelCopyTest {
	private static void channelCopy1(ReadableByteChannel src,
			WritableByteChannel dest) throws IOException {
		// 直接缓冲区
		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// 为写数据做准备
			buffer.flip();
			dest.write(buffer);

			// 压缩此缓冲区
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// Make sure that the buffer is fully drained
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}

	private static void channelCopy2(ReadableByteChannel src,
			WritableByteChannel dest) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// 为读数据做准备
			buffer.flip();
			// Make sure that the buffer was fully drained
			while (buffer.hasRemaining()) {
				dest.write(buffer);
			}
			
			buffer.clear();
		}
	}

	public static void main(String[] argv) throws IOException {
		ReadableByteChannel source = Channels.newChannel(System.in);
		WritableByteChannel dest = Channels.newChannel(System.out);
//		channelCopy1(source, dest);
		channelCopy2 (source, dest); // 可以试试这个
		source.close();
		dest.close();
	}
}
