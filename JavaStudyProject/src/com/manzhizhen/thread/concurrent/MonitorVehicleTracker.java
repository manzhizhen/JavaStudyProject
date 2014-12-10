/**
 * 
 */
package com.manzhizhen.thread.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程安全类的一个示例
 * @author Manzhizhen
 *
 */
public class MonitorVehicleTracker {
	private final Map<String, MutablePoint> locations;
	
	public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
		this.locations = locations;
	}
	
	public synchronized Map<String, MutablePoint> getLocations(){
		return deepCopy(locations);
	}
	
	public synchronized void setLocation(String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		if(loc == null) {
			throw new IllegalArgumentException("No such ID:" + id);
		}
		
		loc.x = x;
		loc.y = y;
	}
	
	private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
		Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
		for(String id : m.keySet()) {
			result.put(id, new MutablePoint(m.get(id)));
		}

		return Collections.unmodifiableMap(result);
	}
	
	
}


