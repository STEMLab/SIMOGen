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
package edu.pnu.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;

/**
 * @author hgryoo
 *
 */
public class StateDijkstraPathFinder {
    
    /** Value for infinite distance  */
    private static final Double INFINITY = Double.MAX_VALUE;
    /** Initial size of the priority queue */
    private static final int PQ_INIT_SIZE = 11;

    /** Map of node distances from the source node */
    private DistanceMap distances;
    /** Set of already visited nodes (where the shortest path is known) */
    private Set<State> visited;
    /** Priority queue of unvisited nodes discovered so far */
    private Queue<State> unvisited;
    /** Map of previous nodes on the shortest path(s) */
    private Map<State, State> prevNodes;
    
    private SpaceLayer graph;
    
    public StateDijkstraPathFinder(SpaceLayer graph) {
        this.graph = graph;
    }
    
    private void init(State node) {
        // create needed data structures
        this.unvisited = new PriorityQueue<State>(PQ_INIT_SIZE, 
                        new DistanceComparator());
        this.visited = new HashSet<State>();
        this.prevNodes = new HashMap<State, State>();
        this.distances = new DistanceMap();
        
        // set distance to source 0 and initialize unvisited queue
        this.distances.put(node, 0);
        this.unvisited.add(node);
    }
    
    public List<State> getShortestPath(State from, State to) {
        return getShortestPathInteranl(from, to);
    }
    
    private List<State> getShortestPathInteranl(State from, State to) {
        List<State> coords = new LinkedList<State>();
        
        if (from.equals(to)) { // source and destination are the same
            coords.add(from); // return a list containing only source node
        } else {
            init(from);
            State node = null;
            while ((node = unvisited.poll()) != null) {
                    if (node == to) {
                            break; // we found the destination -> no need to search further
                    }
                    visited.add(node); // mark the node as visited
                    updateDistance(node); // add/update neighbor nodes' distances
            }
            
            // now we either have the path or such path wasn't available
            if (node == to) { // found a path
                    coords.add(0,to); 
                    State prev = prevNodes.get(to); 
                    while (prev != from) { 
                            coords.add(0, prev);      // always put previous node to beginning
                            prev = prevNodes.get(prev);
                    }       
                    coords.add(0, from); // finally put the source node to first node
            }
        }
        return coords;
    }
    
    private void updateDistance(State node) {
        double nodeDist = distances.get(node);
        for (State n : node.getNeighbors()) {
                if (visited.contains(n)) {
                        continue; // skip visited nodes
                }

                // n node's distance from path's source node
                double nDist = nodeDist + getDistance(node, n);
                
                if (distances.get(n) > nDist) { // stored distance > found dist?
                        prevNodes.put(n, node);
                        setDistance(n, nDist);
                }
        }
    }
    
    private void setDistance(State n, double distance) {
        unvisited.remove(n); // remove node from old place in the queue
        distances.put(n, distance); // update distance
        unvisited.add(n); // insert node to the new place in the queue
    }
    
    private double getDistance(State from, State to) {
        Transition t = from.getConnectWith(to);
        LineString l = t.getGeometry();
        Coordinate[] cs = l.getCoordinates();
        
        double distance = 0.0f;
        for(int i = 0; i < cs.length - 1; i++) {
            distance += GeometryUtil.distance(
                    cs[i],
                    cs[i + 1]);
        }
        return distance;
    }
    
    private class DistanceComparator implements Comparator<State> {
        public int compare(State node1, State node2) {
                double dist1 = distances.get(node1);
                double dist2 = distances.get(node2);
                
                if (dist1 > dist2) {
                        return 1;
                }
                else if (dist1 < dist2) {
                        return -1;
                }
                else {
                        return node1.getId().compareTo(node1.getId());
                }
        }
    }
    
    private class DistanceMap {
        private HashMap<State, Double> map;
        
        public DistanceMap() {
                this.map = new HashMap<State, Double>(); 
        }
        
        public double get(State node) {
                Double value = map.get(node);
                if (value != null) {
                        return value;
                }
                else {
                        return INFINITY;
                }
        }
        
        public void put(State node, double distance) {
                map.put(node, distance);
        }
        
        public String toString() {
                return map.toString();
        }
    }
    
}
