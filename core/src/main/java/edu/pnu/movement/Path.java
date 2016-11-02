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
package edu.pnu.movement;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author hgryoo
 *
 */
public class Path {
    
    private List<Coordinate> coordinates;
    private Coordinate current;
    private int index;
    
    public Path(List<Coordinate> coordinates) {
        this.index = 0;
        this.coordinates = coordinates;
        this.current = this.coordinates.get(index);
    }
    
    public boolean hasNext() {
        if(index + 1 < coordinates.size()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean advance() {
        if(hasNext()) {
            index++;
            current = coordinates.get(index);
            return true;
        }
        return false;
    }
    
    public Coordinate getNext(double velocity) {
        if(hasNext()) {
            return coordinates.get(index + 1);
        } else {
            return getLast();
        }
    }
    
    public Coordinate getLast() {
        return coordinates.get(coordinates.size() - 1);
    }
    
    public Coordinate getCurrent()  {
        return current;
    }
}
