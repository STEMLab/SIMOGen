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

import edu.pnu.core.Clock;
import edu.pnu.core.Generator;
import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.movingobject.ClientObject;
import edu.pnu.model.movingobject.EmployeeObject;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.util.StateDijkstraPathFinder;

/**
 * @author hgryoo
 *
 */
public class IntegratedWaypointTest {

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
    /*
    @Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);

        //State s = (State) sit.next();
        //MovingObject mo = new MovingObject(gen, s);
        //gen.addMovingObject(mo);
        
        int empolyee = 0;
        int client = 0;
        int sum = 0;
        
        Clock clock = gen.getClock();
        while(gen.advance()) {
            
            if(clock.getTime() < 300) {
                if(clock.getTime() % 4 == 0) {
                    Iterator sit = layer.getEntrances().iterator();
                    while(sit.hasNext()) {
                        if(new Random().nextDouble() < 0.5) {
                            State ent = (State) sit.next();
                            State random = getRandomState(ent);
                            MovingObject mo = new EmployeeObject(gen, ent, random);
                            gen.addMovingObject(mo);
                            empolyee++;
                        }
                    }
                }
            } else {
                
                if(clock.getTime() < gen.END - 200) {
                
                    if(clock.getTime() % 5 == 0) {
                        Iterator sit = layer.getEntrances().iterator();
                        while(sit.hasNext()) {
                            if(new Random().nextDouble() < 0.5) {
                                State ent = (State) sit.next();
                                MovingObject mo = new ClientObject(gen, ent);
                                
                                mo.setLife(new Random().nextInt((int) ((gen.END - clock.getTime())/4)) + 100);
                                
                                
                                gen.addMovingObject(mo);
                                client++;
                            }
                        }
                    }
                }
            }

            sum += empolyee + client;
        }

        System.out.println(empolyee + "," + client);
        System.out.println(sum / clock.getTime());
        
        SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("realTest");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject m = it.next();
            csvExt.addHistory(m.getId(), m.getHistory());
        }
        csvExt.bufferedExport("target/integrated_walk.csv");
    }
    */
}
