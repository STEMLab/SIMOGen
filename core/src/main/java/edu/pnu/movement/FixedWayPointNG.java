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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.math.Vector3D;
import com.vividsolutions.jts.operation.distance3d.Distance3DOp;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.model.primal.CellSpace;
import edu.pnu.util.GeometryUtil;
import edu.pnu.util.StateDijkstraPathFinder;

/**
 * @author hgryoo
 *
 */
public class FixedWayPointNG implements Movement {
    
    private int idx = 0;
    private List<State> path;
    
    private Queue<Coordinate> localPath = new LinkedList<Coordinate>();
    
    private StateDijkstraPathFinder finder;
    private SpaceLayer layer;
    private Coordinate next = null;
    
    public FixedWayPointNG(SpaceLayer layer, MovingObject mo, State waypoint) {
        this.layer = layer;
        finder = new StateDijkstraPathFinder(layer);
        
        CellSpace currentCell = mo.getCurrentCellSpace();
        State currentState = currentCell.getDuality();
        
        State destState = waypoint;
        path = finder.getShortestPath(currentState, destState);
        if(path.size() == 1) path.add(path.get(0));
    }

    private State getCurrentState() {
        return path.get(idx);
    }
    
    private State getNextState() {
        if(idx + 1 < path.size()) {
            return path.get(idx + 1);
        }
        return null;
    }
    
    public Coordinate getNext(MovingObject mo, double time) {
        if(getNextState() == null) {
            return null;
        }
        
        Coordinate origin = mo.getCurrentPosition();
        
        if(next == null) {
            //if(new Random().nextDouble() < 0.1) {
                Transition nextTransition = getCurrentState().getConnectWith(getNextState());
                if(nextTransition != null) {
                    Point currentPoint = GeometryUtil.getGeometryFactory().createPoint(origin);     
                    Coordinate[] nearestPs = Distance3DOp.nearestPoints(currentPoint, nextTransition.getGeometry());
                    
                    for(Coordinate nextCandidate : nearestPs) {
                        if(!origin.equals3D(nextCandidate)) {
                            Coordinate candidate = new Coordinate(nextCandidate.x, nextCandidate.y, origin.z);
                            candidate = GeometryUtil.getNoisedCoordinate(candidate, 1, 3);
                            localPath.add(candidate);
                            
                            break;
                        }
                    }
                }
                
                if(localPath.isEmpty()) {
                    origin = GeometryUtil.getNoisedCoordinate(origin, 1, 3);
                    localPath.add(origin);
                }
                
                if(nextTransition != null) {
                Coordinate peek = localPath.peek();
                Coordinate nextStateCoord = getNextState().getPoint().getCoordinate();
                
                Coordinate[] coords = nextTransition.getGeometry().getCoordinates();
                for(Coordinate c : coords) {
                    if(!c.equals3D(nextStateCoord) && !c.equals3D(peek)) {
                        double dot = Vector3D.dot(peek, nextStateCoord, c, nextStateCoord);
                        if(dot > 0) {
                            c = GeometryUtil.getNoisedCoordinate(c, 1, 3);
                            localPath.add(c);
                        }
                    }
                }
                
                nextStateCoord = GeometryUtil.getNoisedCoordinate(nextStateCoord, 1, 3);
                localPath.add(nextStateCoord);
                }
                next = localPath.poll();
        }
        
        double totalDist = mo.getVelocity() * time;
        double nextDist = GeometryUtil.distance(origin, next);
        Coordinate nextStep = null;
        
        if(totalDist < nextDist) {
            nextStep = GeometryUtil.fromTo(origin, next, totalDist);
            totalDist = 0;
        } else {
            nextStep = next;
            if(localPath.isEmpty()) {
                next = null;
                
                mo.setCurrentCellSpace(getNextState().getDuality());
                String type = (String) mo.getCurrentCellSpace().getUserData().get("USAGE");
                if(type.equalsIgnoreCase("ROOM")) {
                    mo.setVelocity(0.4);
                } else if(type.equalsIgnoreCase("CORRIDOR")) {
                    mo.setVelocity(1.0);
                } else if(type.equalsIgnoreCase("DOOR")) {
                    mo.setVelocity(0.9);
                } else {
                    mo.setVelocity(1.2);
                }
                
                idx++;
            } else {
                next = localPath.poll();
            }
            totalDist -= nextDist;
        }
        
        return nextStep;
    }
}
