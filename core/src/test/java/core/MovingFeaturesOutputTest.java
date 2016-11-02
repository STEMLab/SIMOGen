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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.io.SimpleMovingFeaturesExporter;
import edu.pnu.model.History;

/**
 * @author hgryoo
 *
 */
public class MovingFeaturesOutputTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void emptyTest() {
        SimpleMovingFeaturesExporter exporter = new SimpleMovingFeaturesExporter("emptyTest");
        String output = exporter.export();
        System.out.println(output);
    }
    
    @Test
    public void simpleSingleObjectGenerateTest() {
        String mvId = "SingleObject";
        
        List<History> sampleTrajectory = new LinkedList<History>();
        
        Random random = new Random();
        
        Coordinate coord = new Coordinate(0, 0, 0);
        for(int i = 0; i < 1000; i++) {
            History h = new History(i, coord);
            sampleTrajectory.add(h);

            double randX = (random.nextDouble() - 0.5) * 2;
            double randY = (random.nextDouble() - 0.5) * 2;
            
            Coordinate newCoord = new Coordinate(coord.x + randX, coord.y + randY, coord.z);
            coord = newCoord;
        }
        
        SimpleMovingFeaturesExporter exporter = new SimpleMovingFeaturesExporter("emptyTest");
        exporter.addHistory(mvId, sampleTrajectory);
        
        exporter.bufferedExport("target/single_object.gml");
    }
    
    @Test
    public void simpleMassiveObjectGenerateTest() {
        List<List<History>> trajectories = new ArrayList<List<History>>();
        Random random = new Random();
        double maxX = 1000;
        double maxY = 1000;
        int maxZ = 100;
        int MOVING_NUM = 100;
        
        for(int i = 0; i < MOVING_NUM; i++) {
            List<History> history = new LinkedList<History>();
            double randX = random.nextDouble() * maxX;
            double randY = random.nextDouble() * maxY;
            double randZ = random.nextInt(maxZ);
            Coordinate coord = new Coordinate(randX, randY, randZ);
            for(int j = 0; j < 1000; j++) {
                History h = new History(j, coord);
                history.add(h);
    
                randX = (random.nextDouble() - 0.5) * 20;
                randY = (random.nextDouble() - 0.5) * 20;
                
                Coordinate newCoord = new Coordinate(coord.x + randX, coord.y + randY, coord.z);
                coord = newCoord;
            }
            
            trajectories.add(history);
        }
        
/*        SimpleMovingFeaturesExporter exporter = new SimpleMovingFeaturesExporter("massiveTest");
        
        for(int i = 0; i < MOVING_NUM; i++) {
            exporter.addHistory(UUID.randomUUID().toString(), trajectories.get(i));
        }
        exporter.bufferedExport("target/massive_object.gml");*/
        
        SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("massiveTest");
        for(int i = 0; i < MOVING_NUM; i++) {
            csvExt.addHistory(UUID.randomUUID().toString(), trajectories.get(i));
        }
        csvExt.bufferedExport("target/massive_object.csv");
    }
}
