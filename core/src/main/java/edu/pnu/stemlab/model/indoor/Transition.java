/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Map;
import java.util.Set;

import edu.pnu.stemlab.model.geometry.Curve;

/**
 * @author hgryoo
 *
 */
public interface Transition {
	String getId();
	
	Curve getGeometry();
	
	Double getWeight();
	
	SpaceLayer getSpaceLayer();
	
	CellSpaceBoundary getDuality();
	
	Set<State> getConnects();
	
	Map<Object, Object> getUserData();
}
