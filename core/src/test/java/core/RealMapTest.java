package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.pnu.core.Generator;
import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.movingobject.MovingObject;

public class RealMapTest {

    private SpaceLayer layer;
    
    @Before
    public void setUp() throws Exception {
        try {
            SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/Lotte World Mall.gml");
            layer = importer.getSpaceLayer();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        Generator gen = new Generator(layer);
        
        boolean connected = gen.getGraph().isConnectedComponents();
        /*if(!connected){
            throw new IllegalArgumentException();
        }*/
        
        Set<State> ents = layer.getEntrances();
        
        for(State s : ents) {
            MovingObject m1 = new MovingObject(gen, s);
            gen.addMovingObject(m1);
        }
        
        while(gen.advance()) {
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
            MovingObject mo = it.next();
            csvExt.addHistory(mo.getId(), mo.getHistory());
        }
        csvExt.bufferedExport("target/real_test_2.csv");
    }

}
