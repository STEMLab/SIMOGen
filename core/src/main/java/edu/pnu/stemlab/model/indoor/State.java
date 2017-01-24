/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Map;
import java.util.Set;

/**
 * @author hgryoo
 * 
 */
public interface State {
	String getId();
	
	Object getGeometry();
	
	SpaceLayer getSpaceLayer();
	
	Set<Transition> getConnects();
	
	Set<State> getNeighbors();
	
	State getNeighbor(Transition t);
	
	CellSpace getDuality();
	
	Map<Object, Object> getUserData();
	
	boolean isBranch();
}
