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

import java.util.List;
import java.util.Vector;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author hgryoo
 *
 */
public class Transition {
    private String id;
    private LineString line;
    private List<State> connects = new Vector<State>(2);
    
    public Transition(String id, LineString line) {
        this.id = id;
        this.line = line;
    }
    
    public String getId() {
        return id;
    }

    public LineString getGeometry() {
        return line;
    }
    
    public void setState(State a, State b) {
        connects.add(a);
        connects.add(b);
    }
    
    public State getOtherState(State s) {
    	if(s.equals(connects.get(0))) {
    		return connects.get(1);
    	} else if(s.equals(connects.get(1))) {
    		return connects.get(0);
    	} else {
    		return null;
    	}
    }

    @Override
    public String toString() {
        return "Transition [id=" + id + ", line=" + line + "]";
    }
}
