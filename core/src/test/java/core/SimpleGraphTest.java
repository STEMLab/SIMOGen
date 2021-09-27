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

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.pnu.core.Generator;
import edu.pnu.io.SimpleMovingFeaturesExporter;
import edu.pnu.model.History;
import edu.pnu.model.SpaceBuilder;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.movement.FixedWayPoint;

/**
 * @author hgryoo
 *
 */
public class SimpleGraphTest {

    private Coordinate ent1;
    private Coordinate ent2;
    
    private SpaceLayer layer;
    
    @Before
    public void setUp() throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        
        SpaceBuilder builder = new SpaceBuilder();
        
        Coordinate s1Coord = new Coordinate(0, 0, 0);
        Coordinate s2Coord = new Coordinate(0, 10, 0); 
        Coordinate s3Coord = new Coordinate(20, 10, 0); 
        Coordinate s4Coord = new Coordinate(20, 20, 0); 
        Coordinate s5Coord = new Coordinate(20, 0, 0); 
        Coordinate s6Coord = new Coordinate(10, -10, 0); 
        Coordinate s7Coord = new Coordinate(10, -30, 0); 
        Coordinate s8Coord = new Coordinate(50, -50, 0); 
        
        ent1 = s1Coord;
        ent2 = s8Coord;
        
        State s1 = new State("S1", geomFac.createPoint(s1Coord));
        State s2 = new State("S2", geomFac.createPoint(s2Coord));
        State s3 = new State("S3", geomFac.createPoint(s3Coord));
        State s4 = new State("S4", geomFac.createPoint(s4Coord));
        State s5 = new State("S5", geomFac.createPoint(s5Coord));
        State s6 = new State("S6", geomFac.createPoint(s6Coord));
        State s7 = new State("S7", geomFac.createPoint(s7Coord));
        State s8 = new State("S8", geomFac.createPoint(s8Coord));
        
        builder.makeTransition("T1", s1, s2);
        builder.makeTransition("T2", s2, s3);
        builder.makeTransition("T3", s3, s4);
        builder.makeTransition("T4", s4, s5);
        builder.makeTransition("T5", s5, s6);
        builder.makeTransition("T6", s6, s7);
        builder.makeTransition("T7", s7, s8);
        
        layer = builder.buildSpaceLayer();
    }

    @Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);
        
        for(int i = 0; i < 100; i++) {
	        MovingObject m1 = new MovingObject(gen, ent1);
	        gen.addMovingObject(m1);
	        
	        MovingObject m2 = new MovingObject(gen, ent2);
	        gen.addMovingObject(m2);
	        
	        MovingObject m3 = new MovingObject(gen, ent1);
	        //m1.setMovement( new FixedWayPoint(layer, ent2));
	        gen.addMovingObject(m3);
	        
	        MovingObject m4 = new MovingObject(gen, ent2);
	        gen.addMovingObject(m4);
        }
        while(gen.advance());
        
        SimpleMovingFeaturesExporter exporter = new SimpleMovingFeaturesExporter("simpleMap");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject mo = it.next();
            exporter.addHistory(mo.getId(), mo.getHistory());
        }
        exporter.bufferedExport("target/simpleMap.xml");
    }
    
}
