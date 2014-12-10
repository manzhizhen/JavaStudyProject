package com.manzhizhen.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionTest extends ExceptionParent{
	public void catchExceptionTest() throws FileNotFoundException{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
		
		String FILE_PATH = "";
		
		InputStream in = new FileInputStream(new File(FILE_PATH));
		try {
			try {
				// 这里包含可能会抛出异常的代码
				throw new IOException();
			} finally {
				in.close();
			}
		}catch (Throwable e) {
			// 这里可以捕获在finally语句中的异常
			StackTraceElement[] stackTraceElementArray = e.getStackTrace();
			//  StackTraceElement类中包含有获取文件名和当前执行代码行号的方法，
			//  同时还含有获取类名和方法名的方法。
			for(StackTraceElement stackTraceElement : stackTraceElementArray) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("异常发生在类：" + stackTraceElement.getClassName() + "#" + 
						stackTraceElement.getMethodName() +"，  行号：" + stackTraceElement.getLineNumber());
			}
		}
		
		try {
			throw new ArrayIndexOutOfBoundsException("我是故意的！");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException 被我捕获了！");
			throw new RuntimeException("我是故意的！");
		} catch (RuntimeException e) {
			System.out.println("RuntimeException 被我捕获了！");
		} finally {
			System.out.println("靠，搞什么啊！！！");
			throw new FileNotFoundException();
		}
	}
	
	@Override
	public void startSession() throws IOException {
		super.startSession(); 
		throw new FileNotFoundException();
//		throw new RuntimeException();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		new ExceptionTest().catchExceptionTest();
	}
}
