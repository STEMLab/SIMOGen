/**
 * 
 */
package edu.pnu.core.time;

/**
 * @author hgryoo
 *
 */
public interface TimeSource {
	
	int getHours();
	int getMinutes();
	int getSeconds();
	long getTime();
	
}
