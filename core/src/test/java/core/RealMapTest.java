package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.pnu.movement.RandomWalk;
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
        SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/test.xml");
        layer = importer.getSpaceLayer();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        Generator gen = new Generator(layer);

        /*
        boolean connected = gen.getGraph().isConnectedComponents();
        if(!connected){
            throw new IllegalArgumentException();
        }
        */

        Set<State> ents = layer.getEntrances();
        for(State s : ents) {
            MovingObject m1 = new MovingObject(gen, s);
            m1.setMovement(new RandomWalk(m1));
            gen.addMovingObject(m1);
        }
        while(gen.advance());

        SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("realTest");
        Iterator<MovingObject> it = gen.getMovingObjectIterator();
        while(it.hasNext()) {
            MovingObject mo = it.next();
            csvExt.addHistory(mo.getId(), mo.getHistory());
        }
        csvExt.bufferedExport("target/real_test_2.csv");
    }

}
