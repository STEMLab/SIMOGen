/**
 * 
 */
package edu.pnu.core.time;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hgryoo
 *
 */
public abstract class Subject {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	protected void notifyObserver() {
		for(Observer o : observers) {
			o.update();
		}
	}
	
	public void registerObserver(Observer o) {
		if(o != null) {
			observers.add(o);
		}
	}
}