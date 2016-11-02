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
package edu.pnu.model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;
import edu.pnu.model.primal.CellSpace;
import edu.pnu.util.GeometryUtil;

/**
 * @author hgryoo
 *
 */
public class CoordinateGraph {
    
    private Map<Coordinate, List<Coordinate>> adjacencyList
        = new HashMap<Coordinate, List<Coordinate>>();
    
    private Map<Coordinate, State> stateList
        = new HashMap<Coordinate, State>();
    
    private Map<Coordinate, Transition> transitionList
        = new HashMap<Coordinate, Transition>();
    
    private SpaceLayer layer;
    
    private GeometryFactory fac = new GeometryFactory();
    private STRtree index = new STRtree();
    
    public CoordinateGraph(SpaceLayer layer) {
    	this.layer = layer;
        List<Transition> ts = layer.getEdges();
        for(Transition t : ts) {
            addCoordinatefromTransition(t);
        }
        
        List<State> ss = layer.getNodes();
        for(State s : ss) {
            CellSpace c = s.getDuality();
            
            if(c != null) {
            	index.insert(c.getGeometry2D().getEnvelopeInternal(), c);
            	addCoordinatefromState(s);
            }
        }
        index.build();
    }
    
    public State getStateIndex(Coordinate c) {
        Envelope queryEnv = new Envelope(c);
        List<CellSpace> cList = index.query(queryEnv);
        
        Point qPoint = fac.createPoint(c);
        
        List<CellSpace> flatEqual = new ArrayList<CellSpace>();
        for(CellSpace cell : cList) {
            Polygon poly = cell.getGeometry2D();
            if(poly.contains(qPoint)) {
                flatEqual.add(cell);
            }
        }
        
        State result = null;
        double minZ = Double.MAX_VALUE;
        for(CellSpace cell : flatEqual) {
            State s = cell.getDuality();
            Point p = s.getPoint();
            if(p.getCoordinate().z == c.z) {
                result = s;
                break;
            } else {
                double dist = GeometryUtil.distance(p.getCoordinate(), c);
                if(dist < minZ) {
                    minZ = dist;
                    result = s;
                }
           }
        }
        
        return result;
    }
    
    public CellSpace queryCell(Coordinate c) {
    	Envelope queryEnv = new Envelope(c);
    	List<CellSpace> cList = index.query(queryEnv);
    	
    	CellSpace result = null;
    	if(!cList.isEmpty()) {
    		if(cList.size() > 1) {
        		for(int i = 0; i < cList.size(); i++) {
        			if(cList.get(i).getDuality().getPoint().getCoordinate().z - c.z < 0.1) {
        				result = cList.get(i);
        				break;
        			}
        		}
        	} else {
        		result = cList.get(0);
        	}
    	}
    	
    	return result;
    }
    
    public boolean isContained(Coordinate start, Coordinate end) {
    	return true;
    }
    
    public boolean hasCoordinate(Coordinate c) {
        return adjacencyList.containsKey(c);
    }
    
    public List<Coordinate> getCoordinates() {
        return new ArrayList<Coordinate>(adjacencyList.keySet());
    }
    
    public State getState(Coordinate c) {
        return stateList.get(c);
    }
    
    private void addCoordinatefromTransition(Transition t) {
        LineString l = t.getGeometry();
        Coordinate[] coordinates = l.getCoordinates();
        for(int i = 0; i < coordinates.length - 1; i++) {
            registerCoordinate(coordinates[i], coordinates[i + 1]);
            transitionList.put(coordinates[i], t);
            transitionList.put(coordinates[i + 1], t);
        }
    }
    
    private void addCoordinatefromState(State s) {
        Point p = s.getPoint();
        stateList.put(p.getCoordinate(), s);
    }
    
    private void registerCoordinate(Coordinate a, Coordinate b) {
        Coordinate nearestA = getNearestCoordinte(a);
        Coordinate nearestB = getNearestCoordinte(b);
        
        //new coordinate
        /*if(nearestA == null || GeometryUtil.distance(nearestA, a) > 0.0001) {
            List<Coordinate> neighbors = new ArrayList<Coordinate>();
            adjacencyList.put(a, neighbors);
            nearestA = a;
        }
        
        if(nearestB == null || GeometryUtil.distance(nearestB, b) > 0.0001) {
            List<Coordinate> neighbors = new ArrayList<Coordinate>();
            adjacencyList.put(b, neighbors);
            nearestB = b;
        }*/
        
        //adjacencyList.get(nearestA).add(nearestB);
        //adjacencyList.get(nearestB).add(nearestA);
        
        if(!adjacencyList.containsKey(a)) {
            List<Coordinate> neighbors = new ArrayList<Coordinate>();
            adjacencyList.put(a, neighbors);
        }
        adjacencyList.get(a).add(b);
        
        if(!adjacencyList.containsKey(b)) {
            List<Coordinate> neighbors = new ArrayList<Coordinate>();
            adjacencyList.put(b, neighbors);
        }
        adjacencyList.get(b).add(a);
    }
    
    public List<Coordinate> getNeighbors(Coordinate c) {
        Coordinate key = getNearestCoordinte(c);
        List<Coordinate> neighbors = adjacencyList.get(key);
        if(neighbors == null) {
            neighbors = Collections.emptyList();
        }
        return neighbors;
    }
    
    public Coordinate getNearestCoordinte(Coordinate c) {
        Coordinate minKey = null;
        double min = Double.MAX_VALUE;
        Set<Coordinate> coords = adjacencyList.keySet();
        List<Coordinate> array = new ArrayList<Coordinate>(coords);
        for(int i = 0; i < array.size(); i++) {
            Coordinate k = array.get(i);
            double dist = GeometryUtil.distance(k, c);
            if(dist < min) {
                min = dist;
                minKey = k;
            }
        }
        /*for(Coordinate k : adjacencyList.keySet()) {
            double dist = GeometryUtil.distance(k, c);
            if(dist < min) {
                min = dist;
                minKey = k;
            }
        }*/
        return minKey;
    }
    
    public boolean isConnectedComponents() {
        
        Set<Coordinate> visited = new HashSet<Coordinate>();
        Queue<Coordinate> q = new LinkedList<Coordinate>();
        
        Coordinate start = null;
        for(Coordinate u : adjacencyList.keySet()) {
            start = u;
            break;
        }
        
        if(start != null) {
            q.add(start);
        }
        
        while(!q.isEmpty()) {
            Coordinate c = q.poll();
            visited.add(c);
            for(Coordinate v : adjacencyList.get(c)) {
                if(!visited.contains(v)) {
                    q.add(v);
                }
            }
        }
        
        if(visited.size() == adjacencyList.size()) {
            return true;
        } else {
            return false;
        }
    }
}
