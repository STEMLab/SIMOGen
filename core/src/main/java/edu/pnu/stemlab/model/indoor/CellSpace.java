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
public interface CellSpace {
	String getId();
	
	Object getGeometry2D();
	
	Object getGeometry3D();

	PrimalSpace getPrimalSpace();
	
	State getDuality();
	
	Set<CellSpaceBoundary> getPartialBoundedBy();
	
	Map<Object, Object> getUserData();
}