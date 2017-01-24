/**
 * 
 */
package edu.pnu.stemlab.model.geometry;

/**
 * @author hgryoo
 *
 */
public interface Envelope {
    
    Coordinate getLowerCorner();
    
    Coordinate getUpperCorner();
    
    double getWidth();
    
    double getHeight();
    
    double getArea();
    
    Envelope getExpandToInclude(Coordinate e);
    
    Envelope getExpandToInclude(Coordinate lowerCorner, Coordinate upperCorner);
    
    Envelope getExpandToInclude(Envelope e);
    
    Envelope intersection(Envelope env);
    
    boolean intersects(Envelope other);
    
    boolean intersects(Coordinate p);
    
    boolean contains(Envelope other);
    
    boolean contains(Coordinate p);
    
    boolean covers(Coordinate p);
    
    boolean covers(Envelope other);
    
    double distance(Envelope env);
    
    boolean equals(Object other);
    
}