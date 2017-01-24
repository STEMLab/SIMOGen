/**
 * 
 */
package edu.pnu.stemlab.model.geometry;

import java.util.Iterator;

/**
 * @author hgryoo
 *
 */
public interface GeometryCollection extends Geometry {
    
    int getNumGeometries();
    
    Iterator<Geometry> getGeometries();
    
    
}