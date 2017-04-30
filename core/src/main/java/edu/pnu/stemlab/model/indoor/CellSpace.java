/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Map;
import java.util.Set;

import edu.pnu.stemlab.model.geometry.Polygon;
import edu.pnu.stemlab.model.geometry.Solid;

/**
 * @author hgryoo
 *
 */
public interface CellSpace {
	String getId();
	
	Polygon getGeometry2D();
	
	Solid getGeometry3D();

	PrimalSpace getPrimalSpace();
	
	State getDuality();
	
	Set<CellSpaceBoundary> getPartialBoundedBy();
	
	Map<Object, Object> getUserData();
}