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

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.graph.CoordinateGraph;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.util.DijkstraPathFinder;
import edu.pnu.util.GeometryUtil;

/**
 * @author hgryoo
 *
 */
public class FixedWayPoint extends AbstractWayPoint {
    private static final Logger LOGGER = Logger.getLogger(FixedWayPoint.class);
    
    private DijkstraPathFinder finder = null;
    private SpaceLayer layer;
    private CoordinateGraph graph;
    
    public FixedWayPoint(CoordinateGraph graph, Coordinate wayPoint) {
        this.graph = graph;
        this.waypoint = wayPoint;
    }
    
    public Coordinate getNext(MovingObject mo, double time) {
        if(finder == null) {
            finder = new DijkstraPathFinder(graph);
            //TODO START�� END�� State�� Coordinate�̾�� �Ѵ�.
            Coordinate end = graph.getStateIndex(getWaypoint()).getPoint().getCoordinate();
            List<Coordinate> pathCoords = finder.getShortestPath(mo.getCurrentPosition(), end);
            if(pathCoords.isEmpty()) {
                //pathCoords = mo.getPossibleEntrance(mo.getCurrentPosition());
                
                if(pathCoords.isEmpty()) {
                    LOGGER.fatal("DijkstraPathFinder can not found the destiantion");
                    mo.setMovement(new Stop());
                    return mo.getCurrentPosition();
                }
            }
            Path path = new Path(pathCoords);
            setPath(path);
        }
        
        double totalDist = mo.getVelocity() * (time/1000.0);
        Coordinate newCoord = null;
        while(totalDist > 0) {
            Coordinate nextCoord = getPath().getNext(mo.getVelocity());
            double nextDist = GeometryUtil.distance(mo.getCurrentPosition(), nextCoord);
            if(totalDist < nextDist) {
                newCoord = GeometryUtil.fromTo(mo.getCurrentPosition(), nextCoord, totalDist);
                totalDist = 0;
            } else {
                newCoord = nextCoord;
                totalDist -= nextDist;
                getPath().advance();
                if(getPath().hasNext() == false) {
                    break;
                }
            }
        }
        
        //arrived
        double reaminTime = (totalDist / mo.getVelocity());
        if(reaminTime > 0) {
            mo.addHistory(reaminTime, newCoord);
            mo.setMovement(new Stop());
        }
        
        return newCoord;
    }
}
