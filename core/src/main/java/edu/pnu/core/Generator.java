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
package edu.pnu.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.graph.CoordinateGraph;
import edu.pnu.model.movingobject.MovingObject;
/**
 * @author hgryoo
 */
public class Generator {
    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());

    public static final double SAMPLING = 1.0f;
    public static final double END = 1800;

    private List<State> entrances = new ArrayList<>();
    private List<MovingObject> moList = new LinkedList<MovingObject>();
    private Set<MovingObject> dead = new HashSet<MovingObject>();
    private SpaceLayer space;
    private CoordinateGraph graph;
    private Clock clock = Clock.getInstance();

    public Generator(SpaceLayer layer) {
        this.space = layer;
        graph = new CoordinateGraph(space);
        clock.reset();
    }

    public void setSpace(SpaceLayer space) {
        this.space = space;
    }

    public CoordinateGraph getGraph() {
        return graph;
    }

    public SpaceLayer getSpaceLayer() {
        return space;
    }

    public Clock getClock() {
        return clock;
    }

    public boolean advance() {
        if (moList.size() > 0 && moList.size() == dead.size()) {
            return false;
        }
        clock.advance(SAMPLING);
        LOGGER.info("Advanced Clock : " + clock.getTime());

        for (MovingObject m : moList) {
            if (!m.getDead()) {
                m.update(SAMPLING);
            } else {
                dead.add(m);
            }
        }
        return true;
    }

    public void addMovingObject(MovingObject mo) {
        moList.add(mo);
    }

    public Iterator<MovingObject> getMovingObjectIterator() {
        return moList.iterator();
    }

    public Coordinate getNearestEntranceCoord(Coordinate c) {
        //TODO
        return null;
    }

    public void setEntrance(List<State> entrances) {
        this.entrances = entrances;
    }

    public List<State> getEntrance() {
        return entrances;
    }
}