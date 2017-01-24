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
public interface Transition {

	String getId();
	
	Object getGeometry();
	
	Double getWeight();
	
	SpaceLayer getSpaceLayer();
	
	CellSpaceBoundary getDuality();
	
	Set<State> getConnects();
	
	Map<Object, Object> getUserData();
}
