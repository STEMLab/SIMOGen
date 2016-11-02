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

import java.util.Random;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.core.Generator;
import edu.pnu.model.History;
import edu.pnu.model.dual.State;
import edu.pnu.movement.FixedWayPointNG;
import edu.pnu.movement.Movement;
import edu.pnu.movement.RandomWalk;
import edu.pnu.movement.Stop;

/**
 * @author hgryoo
 *
 */
public class EmployeeObject extends MovingObject {
    
    private static final Logger LOGGER = Logger.getLogger(MovingObject.class);
    
    State destination;
    
    public EmployeeObject(Generator gen, State start, State destination) {
        super(gen, start);
        this.destination = destination;
        movement = getDefaultMovement();
        life = new Random().nextInt(200) + 1500;
    }
    
    public Movement getDefaultMovement() {
        return new FixedWayPointNG(gen.getSpaceLayer(), this, destination);
    }
    
    public Movement getNextMovement() {
        return new RandomWalk(this);
    }
    
    public Movement getTerminateMovement() {
        return new FixedWayPointNG(gen.getSpaceLayer(), this, start);
    }
    
    protected History createHistory(double remain, Coordinate c) {
        History h = new History(gen.getClock().getTime() - remain, c);
        h.setUserData("TYPE", 1);
        return h;
    }
}
