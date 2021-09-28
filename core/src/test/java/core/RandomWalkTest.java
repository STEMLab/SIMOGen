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

import org.junit.Before;
import org.junit.Test;

import edu.pnu.core.Generator;
import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.model.MovingObjectNG;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.movingobject.MovingObject;

/**
 * @author hgryoo
 *
 */
public class RandomWalkTest {

private SpaceLayer layer;
    
    @Before
    public void setUp() throws Exception {
        SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/LotteWorldMall.gml");
        layer = importer.getSpaceLayer();
    }
    
    @Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);
        
        Iterator sit = layer.getEntrances().iterator();
        while(sit.hasNext()) {
            State s = (State) sit.next();
            MovingObject mo = new MovingObject(gen, s);
            gen.addMovingObject(mo);
        }
        
        /*State s = (State) sit.next();
        MovingObject mo = new MovingObject(gen, s);
        gen.addMovingObject(mo);*/
        
        int count = 0;
        while(gen.advance()) {
            
            if(count < 10) {
                sit = layer.getEntrances().iterator();
                while(sit.hasNext()) {
                    State s = (State) sit.next();
                    MovingObject mo = new MovingObject(gen, s);
                    gen.addMovingObject(mo);
                }
            }
            count++;
            /*if(new Random().nextInt(10) < 4 && idx < 100) {
                for(State s : ents) {
                    MovingObject m1 = new MovingObject(gen, s);
                    gen.addMovingObject(m1);
                }
            }*/
        }
        
        SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("realTest");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject m = it.next();
            csvExt.addHistory(m.getId(), m.getHistory());
        }
        csvExt.bufferedExport("target/random_walk.csv");
    }

}
