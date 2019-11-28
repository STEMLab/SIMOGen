/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author hgryoo
 *
 */
public interface CellSpace {
	String getId();
	
	Polygon getGeometry2D();

	PrimalSpace getPrimalSpace();
	
	State getDuality();
	
	Set<CellSpaceBoundary> getPartialBoundedBy();
	
	Map<Object, Object> getUserData();
}