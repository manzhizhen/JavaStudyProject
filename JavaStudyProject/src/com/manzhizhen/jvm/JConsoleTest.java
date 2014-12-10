/**
 * 
 */
package com.manzhizhen.jvm;

import java.util.ArrayList;
import java.util.List;


/**
 * JDK的bin目录下的jconsole.exe是一款优秀的虚拟机监控工具
 * 本例的虚拟机参数 -Xms100m -Xmx100m -XX:+UseSerialGC
 * 注意System.gc()前后年轻代和老年代的变化
 * 一个JConsoleTest对象大约占64KB
 * @author Manzhizhen
 *
 */
public class JConsoleTest {
	public byte[] placeholder = new byte[64 * 1024];
	
	public static void fillHeap(int num) throws InterruptedException {
		List<JConsoleTest> list = new ArrayList<JConsoleTest>();
		for(int i = 0; i < num; i++) {
			// 稍作延时，令监视器曲线的变化更加明显
			Thread.sleep(50);
			System.out.println("不断创建JConsoleTest对象");
			list.add(new JConsoleTest());
		}
		
		System.out.println("-----------开始尝试GC--------------");
		System.gc();
	}
	
	public static void main(String[] args) throws InterruptedException {
		fillHeap(1000);
	}
}
