/**
 * 
 */
package edu.pnu.stemlab.model.geometry;

import java.util.List;

/**
 * @author hgryoo
 *
 */
public interface Curve extends Geometry {
    
    List<Coordinate> getCoordinates();
    
}
