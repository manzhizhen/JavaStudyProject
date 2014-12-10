/**
 * 
 */
package com.manzhizhen.jvm;

/**
 * 在JDK1.6和JDK1.7的输出结果不一样
 * @author Manzhizhen
 *
 */
public class RuntimeConstantPoolOOM {
	public static void main(String[] args) {
		String str1 = new StringBuffer("计算机").append("软件").toString();
		System.out.println(str1.intern() == str1);
		
		String str2 = new StringBuffer("ja").append("va").toString();
		System.out.println(str2.intern() == str2);
		
		// JDK1.7  true false
		// JDK1.6  false false
	}
}
