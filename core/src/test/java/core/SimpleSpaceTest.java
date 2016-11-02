/*
 * Indoor Moving Objects Generator
 * Copyright (c) 2016 Pusan National University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package core;

import java.util.List;

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
public class SimpleSpaceTest extends SimpleSpaceTestSupport {

    private SpaceLayer layer;
    
    private Coordinate ent1;
    
    @Before
    public void setUp() throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        
        SpaceBuilder builder = new SpaceBuilder();
        
        Coordinate c1 = new Coordinate(0, 0, 3);
        Coordinate c2 = new Coordinate(10, 10, 3); 
        
        Coordinate c3 = new Coordinate(10, 3, 3);
        Coordinate c4 = new Coordinate(12, 7, 3);
        
        Coordinate c5 = new Coordinate(12, 0, 3);
        Coordinate c6 = new Coordinate(22, 10, 3); 

        Polygon cellPoly1 = createSimplePolygon(c1, c2);
        Polygon cellPoly2 = createSimplePolygon(c3, c4);
        Polygon cellPoly3 = createSimplePolygon(c5, c6);
        
        Coordinate sc1 = cellPoly1.getCentroid().getCoordinate();
        Coordinate sc2 = cellPoly2.getCentroid().getCoordinate();
        Coordinate sc3 = cellPoly3.getCentroid().getCoordinate();
        
        CellSpace cell1 = new CellSpace("C1", cellPoly1);
        CellSpace cell2 = new CellSpace("C2", cellPoly2);
        CellSpace cell3 = new CellSpace("C3", cellPoly3);
        
        State s1 = new State("S1", geomFac.createPoint(sc1), cell1);
        State s2 = new State("S2", geomFac.createPoint(sc2), cell2);
        State s3 = new State("S3", geomFac.createPoint(sc3), cell3);
        
        s1.setDuality(cell1);
        s2.setDuality(cell2);
        s3.setDuality(cell3);
        
        cell1.setDuality(s1);
        cell2.setDuality(s2);
        cell3.setDuality(s3);
        
        builder.makeTransition("T1", s1, s2);
        builder.makeTransition("T2", s2, s3);
        
        layer = builder.buildSpaceLayer();
        
        ent1 = sc1;
    }

    @Test
    public void randomWalkTest() throws Exception {
        
        List<CellSpace> c = layer.getCells();
        
        
        
    }
    
    /*@Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);
        
        for(int i = 0; i < 1; i++) {
	        MovingObject m1 = new MovingObject(gen, ent1);
	        gen.addMovingObject(m1);
        }
        
        while(gen.advance());
        
        SimpleMovingFeaturesCSVExporter exporter = new SimpleMovingFeaturesCSVExporter("simpleMap");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject mo = it.next();
            exporter.addHistory(mo.getId(), mo.getHistory());
        }
        exporter.bufferedExport("target/simpleMap.csv");
    }*/
    
}
