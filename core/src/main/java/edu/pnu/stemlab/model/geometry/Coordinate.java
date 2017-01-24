/**
 * 
 */
package edu.pnu.stemlab.model.geometry;

/**
 * @author hgryoo
 *
 */
public interface Coordinate {
    
    double distance(Coordinate p);
    
    int getDimension();
    
    double[] getCoordinate();
    
    double getOrdinate(int dimension) throws IndexOutOfBoundsException;
    
    Double getX();
    
    Double getY();
    
    Double getZ();
}