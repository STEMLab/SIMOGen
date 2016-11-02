/**
 * 
 */
package core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import edu.pnu.model.SpaceBuilder;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class SimpleSpaceTestSupport {
	
	private SpaceLayer layer;
    private Coordinate ent1;
	
    private GeometryFactory geomFac = new GeometryFactory();
    
    public Polygon createSimplePolygon(Coordinate lower, Coordinate upper) {
    	Coordinate c1 = new Coordinate(lower.x, lower.y, lower.z); //lower
        Coordinate c2 = new Coordinate(upper.x, lower.y, lower.z);
        Coordinate c3 = new Coordinate(upper.x, upper.y, lower.z); //upper
        Coordinate c4 = new Coordinate(lower.x, upper.y, lower.z);
        
        Polygon poly = geomFac.createPolygon(new Coordinate[] { c1, c2, c3, c4, c1 });
        
        return poly;
    }
}
