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
package edu.pnu.model.dual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class State {
    
    private String id;
    private Point point;
    private List<Transition> connects = new ArrayList<Transition>();
    private CellSpace duality;
    private Map<Object, Object> userData;
    
    public State(String id, Point p) {
        this.id = id;
        this.point = p;
    }
    
    public State(String id, Point p, Map<Object, Object> userData) {
        this(id, p);
        this.userData = userData;
    }
    
    public State(String id, Point p, CellSpace duality) {
        this.id = id;
        this.point = p;
        this.duality = duality;
    }
    
    public String getId() {
        return id;
    }
    
    public Point getPoint() {
        return point;
    }
    
    public boolean isBranch() {
        return connects.size() > 2;
    }
    
    public List<State> getNeighbors() {
        List<State> neighbors = new ArrayList<State>();
        for(Transition t : connects) {
            neighbors.add(t.getOtherState(this));
        }
        return neighbors;
    }
    
    public void addConnects(Transition t) {
        connects.add(t);
    }
    
    public Transition getConnectWith(State other) {
        for(Transition t : connects) {
            if(t.getOtherState(this).equals(other)) {
                return t;
            }
        }
        return null;
    }
    
    public CellSpace getDuality() {
        return duality;
    }

    public void setDuality(CellSpace duality) {
        this.duality = duality;
    }

    public Map<Object, Object> getUserData() {
        return userData;
    }
    
    @Override
    public String toString() {
        return "State [id=" + id + ", point=" + point + "]";
    }
}
