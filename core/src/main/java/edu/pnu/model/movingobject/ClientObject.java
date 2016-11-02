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
package edu.pnu.model.movingobject;

import java.util.List;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.core.Generator;
import edu.pnu.model.History;
import edu.pnu.model.dual.State;
import edu.pnu.movement.FixedWayPointNG;
import edu.pnu.movement.Movement;
import edu.pnu.movement.RandomWayPointNG;

/**
 * @author hgryoo
 *
 */
public class ClientObject extends MovingObject {

    public ClientObject(Generator gen, State state) {
        super(gen, state);
        movement = getDefaultMovement();
        life = new Random().nextInt(1500) + 100;
    }
    
    public Movement getDefaultMovement() {
        return new RandomWayPointNG(gen.getSpaceLayer(), this);
    }
    
    public Movement getNextMovement() {
        return new RandomWayPointNG(gen.getSpaceLayer(), this);
    }
    
    public Movement getTerminateMovement() {
        return new FixedWayPointNG(gen.getSpaceLayer(), this, start);
    }
    
    protected History createHistory(double remain, Coordinate c) {
        History h = new History(gen.getClock().getTime() - remain, c);
        h.setUserData("TYPE", 2);
        return h;
    }
    
    private List<State> getNearestPossibleEntrance() {
        return null;
    }
    
}
