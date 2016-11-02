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
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Triangle;

import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.model.primal.CellSpace;
import edu.pnu.util.GeometryUtil;

/**
 * @author hgryoo
 *
 */
public class RandomWalk implements Movement {
    
    private CellSpace walkingCell;
    private Polygon currentTriangle;
    private Coordinate next;
    
    public Coordinate getNext(MovingObject mo, double time) {
        
        if(next == null) {
            List<Polygon> adjacency = walkingCell.getAdjacencyTriangle(currentTriangle);
            //if(adjacency.size() - 1 < 1) {
            //    System.out.println();
            //}
            int randNumber = new Random().nextInt(adjacency.size());
            Polygon randTriangle = adjacency.get(randNumber);
            next = GeometryUtil.getRandomPoint(GeometryUtil.getPolygontoTriangle(randTriangle));
        }
        
        double totalDist = mo.getVelocity() * time;
        
        double nextDist = GeometryUtil.distance(mo.getCurrentPosition(), next);
        Coordinate nextStep = null;
        
        if(totalDist < nextDist) {
            nextStep = GeometryUtil.fromTo(mo.getCurrentPosition(), next, totalDist);
            totalDist = 0;
        } else {
            nextStep = next;
            next = null;
            totalDist -= nextDist;
        }
        
        return nextStep;
    }
    
    public RandomWalk(MovingObject mo) {
        Coordinate current = mo.getCurrentPosition();
        CellSpace currentCell = mo.getCurrentCellSpace();
        walkingCell = currentCell;
        
        
        List<Polygon> ts = walkingCell.getTriangles();
        for(Polygon t : ts) {
            if(GeometryUtil.pointInTriangle(t, current)) {
                currentTriangle = t;
                break;
            }
        }
        
        if(currentTriangle == null) {
            double minValue = Double.MAX_VALUE;
            Polygon min = null;
            for(Polygon t : ts) {
                double dist = 
                        GeometryUtil.getGeometryFactory().createPoint(current).distance(t);
                if(dist < minValue) {
                    minValue = dist;
                    min = t;
                }
            }
            currentTriangle = min;
        }
        
    }
    
}
