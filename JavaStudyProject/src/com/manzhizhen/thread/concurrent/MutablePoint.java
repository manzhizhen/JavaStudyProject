/**
 * 
 */
package com.manzhizhen.thread.concurrent;

/**
 * 展示线程安全示例中的数据对象
 * 本类为MonitorVehicleTracker中使用的数据对象
 * @author Manzhizhen
 *
 */
public class MutablePoint {
	public int x, y;
	
	public MutablePoint() {
		x = 0;
		y = 0;
	}
	
	public MutablePoint(MutablePoint p ) {
		this.x = p.x;
		this.y = p.y;
	}
}
