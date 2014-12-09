/**
 * 
 */
package com.sugarmq.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;

/**
 * Session 的 ID 生成器
 * ID总位数为21位
 * @author manzhizhen
 *
 */
public class SessionIdgenerate {
	private static final String DATE_FORMAT = "yyyyMMddHHmmssSSS";
	private static final int OTHER_NUM_LENGTH = 4;
	
	private static volatile String lastTimeString = "";
	private static volatile int index = 0;
	
	/**
	 * 返回一个唯一的Session ID
	 * @return
	 */
	public static String getNewSessionId() throws JMSException{
		synchronized (lastTimeString) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
			String startNum = simpleDateFormat.format(new Date());
			if(startNum.compareTo(lastTimeString) > 0) {
				index = 0;
				lastTimeString = startNum;
			} else if (startNum.compareTo(lastTimeString) == 0) {
				if(index > (Math.pow(10, OTHER_NUM_LENGTH + 1) - 1)) {
					throw new JMSException("生成Session ID出错：其他数据位数不足！");
				}
				
				index++;
			}
			
			// 格式化字符串并返回
			return startNum + String.format("%1$0" + OTHER_NUM_LENGTH + "d", index);
		}
	}
}
