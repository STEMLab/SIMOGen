/**
 * 
 */
package edu.pnu.core.time;

import java.util.GregorianCalendar;

/**
 * @author hgryoo
 *
 */
public class Clock extends Subject implements TimeSource {
	
	private GregorianCalendar time;
	
	public Clock() {
		time = new GregorianCalendar();
	}
	
	public Clock(GregorianCalendar calendar) {
		time = calendar;
	}
	
	public int getHours() {
		return time.get(GregorianCalendar.HOUR_OF_DAY);
	}

	public int getMinutes() {
		return time.get(GregorianCalendar.MINUTE);
	}

	public int getSeconds() {
		return time.get(GregorianCalendar.SECOND);
	}

	public long getTime() {
		return time.getTimeInMillis();
	}
}
