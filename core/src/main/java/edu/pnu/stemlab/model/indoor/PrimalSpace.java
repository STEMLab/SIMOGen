/**
 * 
 */
package edu.pnu.stemlab.model.indoor;

import java.util.Collection;

/**
 * @author hgryoo
 *
 */
public interface PrimalSpace {

	Collection<CellSpace> getCellSpaces();
	
	Collection<CellSpaceBoundary> getCellSpaceBoundaries();
}
