/**
 * 
 */
package com.manzhizhen.nio.channel;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 使用通道传输进行文件连结
 * 
 * 由于经常需要从一个位置将文件数据批量传输到另一个位置，FileChannel类添加了一些优化
方法来提高该传输过程的效率
 * @author Manzhizhen
 * 
 */
public class ChannelTransfer {
	public static void main(String[] argv) throws Exception {
		if (argv.length == 0) {
			System.err.println("Usage: filename ...");
			return;
		}
		catFiles(Channels.newChannel(System.out), argv);
	}

	// Concatenate the content of each of the named files to
	// the given channel. A very dumb version of 'cat'.
	private static void catFiles(WritableByteChannel target, String[] files)
			throws Exception {
		for (int i = 0; i < files.length; i++) {
			FileInputStream fis = new FileInputStream(files[i]);
			FileChannel channel = fis.getChannel();
			channel.transferTo(0, channel.size(), target);
			channel.close();
			fis.close();
		}
	}
}
