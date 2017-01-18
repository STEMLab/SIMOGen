/**
 * 
 */
package edu.pnu.stemlab.model;

import java.util.Collection;

/**
 * @author hgryoo
 *
 */
public interface SpaceLayer {
	
	Collection<State> getNodes();
	
	Collection<Transition> getEdges();
}
