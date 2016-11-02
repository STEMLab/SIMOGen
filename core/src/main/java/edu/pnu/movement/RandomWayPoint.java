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
import edu.pnu.model.graph.CoordinateGraph;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.util.DijkstraPathFinder;
import edu.pnu.util.GeometryUtil;

/**
 * @author hgryoo
 *
 */
public class RandomWayPoint extends AbstractWayPoint {    
    private static final Logger LOGGER = Logger.getLogger(RandomWayPoint.class);
    
    private DijkstraPathFinder finder = null;
    private SpaceLayer layer;
    private CoordinateGraph graph;
    
    public RandomWayPoint(CoordinateGraph graph) {
        this.graph = graph;
    }
    
    protected Coordinate getRandomCoordinate() {
        /* TODO we need any point on graph, 
         * not coordinates of transition poslist 
         */
        List<Coordinate> coordinates = graph.getCoordinates();
        int randNumber = new Random().nextInt(coordinates.size() - 1);
        return coordinates.get(randNumber);
    }
    
    public Coordinate getNext(MovingObject mo, double time) {
        if(finder == null) {
            finder = new DijkstraPathFinder(graph);
            
            Coordinate randomDest = getRandomCoordinate();
            setWaypoint(randomDest);
            
            Coordinate fromOn = graph.getNearestCoordinte(mo.getCurrentPosition());
            Coordinate toOn = graph.getNearestCoordinte(getWaypoint());
            
            //TODO ����� START�� END�� State�� Coordinate�̾�� �Ѵ�.
            List<Coordinate> pathCoords = finder.getShortestPath(fromOn, toOn);
            if(pathCoords.isEmpty()) {
                LOGGER.fatal("DijkstraPathFinder can not found the destiantion");
                pathCoords.add(mo.getCurrentPosition());
                pathCoords.add(graph.getNeighbors(mo.getCurrentPosition()).get(0));
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
        
        double reaminTime = (totalDist / mo.getVelocity());
        if(reaminTime > 0) {
            mo.addHistory(reaminTime, newCoord);
            mo.setMovement(mo.getNextMovement());
            //MovingObject�� �ð� �߿� �����ٰ� ��ȣ�� �����
            
            //newCoord = getNext(mo, totalDist/mo.getVelocity());
        }
        
        return newCoord;
    }
}
