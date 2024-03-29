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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.operation.distance3d.Distance3DOp;

import edu.pnu.core.Clock;
import edu.pnu.core.Generator;
import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.movingobject.EmployeeObject;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.model.primal.CellSpace;
import edu.pnu.util.StateDijkstraPathFinder;

/**
 * @author hgryoo
 *
 */
public class EmployeeWayPointTest {

private SpaceLayer layer;
    
    @Before
    public void setUp() throws Exception {
        SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/test.xml");
        layer = importer.getSpaceLayer();
    }
    
    private State getRandomState(State s) {
        List<State> sameSection = layer.getNodesBySection(
                (String) s.getDuality().getUserData().get("SECTION"));
        List<State> roomStates = layer.getNodesByUsage("ROOM");
        List<State> states = new ArrayList<State>(sameSection);
        states.retainAll(roomStates);
        
        int stateSize = states.size();
        
        StateDijkstraPathFinder finder = new StateDijkstraPathFinder(layer);
        State random = null;
        List path = null;
        do {
            int randNumber = new Random().nextInt(stateSize - 1);
            random = states.get(randNumber);
            path = finder.getShortestPath(s, random);
        } while(path.size() == 0);
        return random;
    }

    final int TIME_DURATION = 60;
    final int MAX_MO_COUNT = 10;
    final double GENERATE_PROBABILITY = 0.2;
    /*@Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);

        Iterator sit = layer.getEntrances().iterator();
        State s = (State) sit.next();
        State random = getRandomState(s);
        MovingObject mo = new EmployeeObject(gen, random, random);
        gen.addMovingObject(mo);
        
        int moCount = 0;
        Clock clock = gen.getClock();
        while(gen.advance()) {
            if(clock.getTime() < TIME_DURATION) {
                if(clock.getTime() % 5 == 0) {
                    sit = layer.getEntrances().iterator();
                    while(sit.hasNext()) {
                        State ent = (State) sit.next();
                        if(new Random().nextDouble() < GENERATE_PROBABILITY && moCount < MAX_MO_COUNT ) {
                            random = getRandomState(ent);
                            mo = new EmployeeObject(gen, ent, random);
                            gen.addMovingObject(mo);
                            moCount ++;
                        }
                    }
                }
            }
        }
        
        SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("realTest");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject m = it.next();
            csvExt.addHistory(m.getId(), m.getHistory());
        }
        csvExt.bufferedExport("target/employee_walk.csv");
    }*/

}
