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
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.math.Vector3D;
import com.vividsolutions.jts.operation.distance3d.Distance3DOp;

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
public class RandomWayPointNG implements Movement {
    
    private int idx = 0;
    private List<State> path;
    
    private Queue<Coordinate> localPath = new LinkedList<Coordinate>();
    
    private StateDijkstraPathFinder finder;
    private SpaceLayer layer;
    private Coordinate next = null;
    
    public RandomWayPointNG(SpaceLayer layer, MovingObject mo) {
        this.layer = layer;
        finder = new StateDijkstraPathFinder(layer);
        
        CellSpace currentCell = mo.getCurrentCellSpace();
        State currentState = currentCell.getDuality();
        
        do {
            State destState = getRandomState();
            path = finder.getShortestPath(currentState, destState);
        } while(path == null);
    }
    
    private State getRandomState() {
        int stateSize = layer.getNodes().size();
        int randNumber = new Random().nextInt(stateSize - 1);
        return layer.getNodes().get(randNumber);
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
    
    private Coordinate getNoisedCoordinate(Coordinate origin, State s) {
        Polygon currentPoly = s.getDuality().getTriangle(origin);
        
        if(currentPoly == null) {
            System.out.println();
        }
        
        return GeometryUtil.getRandomPoint(currentPoly);
    }
    
    public Coordinate getNext(MovingObject mo, double time) {
        if(getNextState() == null) {
            return null;
        }
        
        Coordinate origin = mo.getCurrentPosition();
        
        if(next == null) {
            //if(new Random().nextDouble() < 1.1) {
                Transition nextTransition = getCurrentState().getConnectWith(getNextState());
                Point currentPoint = GeometryUtil.getGeometryFactory().createPoint(origin);     
                Coordinate[] nearestPs = Distance3DOp.nearestPoints(currentPoint, nextTransition.getGeometry());
                
                for(Coordinate nextCandidate : nearestPs) {
                    if(!origin.equals3D(nextCandidate)) {
                        Coordinate candidate = new Coordinate(nextCandidate.x, nextCandidate.y, origin.z);
                        candidate = getNoisedCoordinate(origin, getCurrentState());
                        localPath.add(candidate);
                        break;
                    }
                }
                
                if(localPath.isEmpty()) {
                    Coordinate noise = getNoisedCoordinate(origin, getCurrentState());
                    localPath.add(noise);
                }
                
                Coordinate peek = localPath.peek();
                Coordinate nextStateCoord = getNextState().getPoint().getCoordinate();
                
                Coordinate[] coords = nextTransition.getGeometry().getCoordinates();
                for(Coordinate c : coords) {
                    if(!c.equals3D(nextStateCoord) && !c.equals3D(peek)) {
                        double dot = Vector3D.dot(peek, nextStateCoord, c, nextStateCoord);
                        if(dot > 0) {
                            c = getNoisedCoordinate(c, getCurrentState());
                            localPath.add(c);
                        }
                    }
                }
                
                nextStateCoord = getNoisedCoordinate(nextStateCoord, getNextState());
                localPath.add(nextStateCoord);
                next = localPath.poll();
            //}
        }

        double totalDist = mo.getVelocity() * time;
        
        /*int cnt = 0;
        if(next == null) {
            //do {
                Coordinate noisedCoordinate = null;
                Vector3D noised = GeometryUtil.getNoiseVector(3, 3);
                
                CellSpace resultCell = null;
                do {
                    noisedCoordinate = new Coordinate
                                    (origin.x + noised.getX(), origin.y + noised.getY() , origin.z + noised.getZ());
                    resultCell = layer.getCellSpace(noisedCoordinate);
                    noised = new Vector3D(noised.getX() / 2, noised.getY() / 2, noised.getZ() / 2);
                    
                    if(cnt == 5) {
                        break;
                    }
                    System.out.println(noised.toString() + "," + cnt++);
                    
                } while (!originCell.equals(resultCell));
                
                next = noisedCoordinate;
                //break;
                
                //System.out.println(cnt++);
            //} while (true);
        } */
        
        double nextDist = GeometryUtil.distance(origin, next);
        Coordinate nextStep = null;
        
        //while(totalDist > 0) {
            if(totalDist < nextDist) {
                nextStep = GeometryUtil.fromTo(origin, next, totalDist);
                totalDist = 0;
            } else {
                nextStep = next;
                if(localPath.isEmpty()) {
                    next = null;
                    mo.setCurrentCellSpace(getNextState().getDuality());
                    
                    String type = (String) mo.getCurrentCellSpace().getUserData().get("USAGE");
                    if(type == null) {
                        mo.setVelocity(1.0);
                    } else if(type.equalsIgnoreCase("ROOM") || type.equalsIgnoreCase("DOOR")) {
                        mo.setVelocity(0.4);
                    } else if(type.equalsIgnoreCase("CORRIDOR")) {
                        mo.setVelocity(1.0);
                    } else {
                        mo.setVelocity(1.5);
                    }
                    
                    idx++;
                } else {
                    next = localPath.poll();
                }
                totalDist -= nextDist;
            }
        //}
        
        return nextStep;
    }
}
