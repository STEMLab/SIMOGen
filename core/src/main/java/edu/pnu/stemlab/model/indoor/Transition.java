/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author hgryoo
 *
 */
public interface Transition {
	String getId();

	LineString getGeometry();
	
	Double getWeight();
	
	SpaceLayer getSpaceLayer();
	
	CellSpaceBoundary getDuality();
	
	Set<State> getConnects();
	
	Map<Object, Object> getUserData();
}
