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
package edu.pnu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Triangle;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.distance3d.Distance3DOp;
import com.vividsolutions.jts.operation.distance3d.PlanarPolygon3D;

import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;
import edu.pnu.model.index.CellSpaceIndex;
import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class SpaceLayer implements CellSpaceIndex {
	
    protected List<State> nodes;
    protected Map<String, State> nodesMap;
    
    protected List<Transition> edges;
    protected Map<String, Transition> edgesMap;
    
    protected List<CellSpace> cells;
    protected Map<String, CellSpace> cellsMap;
    
    protected STRtree polygonIdx;
    
    /* entrances set */
    protected Set<State> entrances = new HashSet<State>();
    
    protected Map<String, List<State>> usageMap = new HashMap<String, List<State>>();
    protected Map<String, List<State>> sectionMap = new HashMap<String, List<State>>();
    
    public SpaceLayer() {
        nodes = new ArrayList<State>();
        edges = new ArrayList<Transition>();
        cells = new ArrayList<CellSpace>();
        
        nodesMap = new HashMap<String, State>();
        edgesMap = new HashMap<String, Transition>();
        cellsMap = new HashMap<String, CellSpace>();
    }
    
    public void addState(State s) {
        nodes.add(s);
        nodesMap.put(s.getId(), s);
        
        Map userData = s.getUserData();
        if(userData != null) {
            if(userData.containsKey("USAGE")) {
                String usage = (String) userData.get("USAGE");
                if(usage.equalsIgnoreCase("ENTRANCE")) {
                    entrances.add(s);
                }
                
                if(!usageMap.containsKey(usage)) {
                    usageMap.put(usage, new ArrayList<State>());
                }
                usageMap.get(usage).add(s);
            }
        }
    }

    public State getState(String id) {
        if(nodesMap.containsKey(id)) {
            return nodesMap.get(id);
        }
        return null;
    }
    
    public Set<State> getEntrances() {
        return entrances;
    }
    
    public void addTransition(Transition t) {
        edges.add(t);
        edgesMap.put(t.getId(), t);
    }
    
    public Transition getTransition(String id) {
        if(edgesMap.containsKey(id)) {
            return edgesMap.get(id);
        }
        return null;
    }
    
    public void addCellSpace(CellSpace c) {
        cells.add(c);
        cellsMap.put(c.getId(), c);
        
        Map userData = c.getUserData();
        if(userData != null) {
            if(userData.containsKey("USAGE")) {
                String usage = (String) userData.get("USAGE");
                if(!usageMap.containsKey(usage)) {
                    usageMap.put(usage, new ArrayList<State>());
                }
                usageMap.get(usage).add(c.getDuality());
            }
            if(userData.containsKey("SECTION")) {
                String section = (String) userData.get("SECTION");
                if(!sectionMap.containsKey(section)) {
                    sectionMap.put(section, new ArrayList<State>());
                }
                sectionMap.get(section).add(c.getDuality());
            }
        }
    }
    
    public CellSpace getCellSpace(String id) {
        if(cellsMap.containsKey(id)) {
            return cellsMap.get(id);
        }
        return null;
    }
    
    public List<State> getNodes() {
        return nodes;
    }
    
    public List<State> getNodesByUsage(String usage) {
        return usageMap.get(usage);
    }
    
    public List<State> getNodesBySection(String section) {
        return sectionMap.get(section);
    }
    
    public List<Transition> getEdges() {
        return edges;
    }
    
    public List<CellSpace> getCells() {
        return cells;
    }
    
    protected void removeCellSpace(String id) {
        CellSpace c = cellsMap.get(id);
        if(c != null) {
            cells.remove(c);
            cellsMap.remove(id);
        }
    }
    
    protected void removeState(String id) {
        State s = nodesMap.get(id);
        if(s != null) {
            nodes.remove(s);
            nodesMap.remove(id);
        }
    }
    
    public void buildIndex() {
        //optimize unconnected states
        Iterator<State> sIt = nodes.iterator();
        List<String> toRemove = new ArrayList<String>();
        while(sIt.hasNext()) {
            State s = sIt.next();
            if(s.getNeighbors().size() == 0) {
                
                CellSpace duality = s.getDuality();
                if(duality != null) {
                    String cellId = duality.getId();
                    removeCellSpace(cellId);
                }
                toRemove.add(s.getId());
            }
        }
        for(String sId : toRemove) {
            removeState(sId);
        }
        
        polygonIdx = new STRtree();
        for(CellSpace c : cells) {
            Polygon p = c.getGeometry2D();
            polygonIdx.insert(p.getEnvelopeInternal(), c);
        }
        polygonIdx.build();
        
        /*for(CellSpace c : cells) {
            List<Polygon> triangles = c.getTriangles();
            System.out.println();
        }*/
    }
    
    public CellSpace getCellSpace(Coordinate c) {

        Envelope queryEnv = new Envelope(c);
        List<CellSpace> cList = polygonIdx.query(queryEnv);
        
        CellSpace result = null;
        if(cList != null) {
            Point qPoint = new GeometryFactory().createPoint(c);
            
            //refinement
            List<CellSpace> flatEqual = new ArrayList<CellSpace>();
            for(CellSpace cell : cList) {
                PlanarPolygon3D planar = cell.getPlanarGeometry2D();
                if(planar.intersects(c)) {
                    flatEqual.add(cell);
                }
            }
            
            CellSpace min = null;
            double minValue = Double.MAX_VALUE;
            for(CellSpace cell : flatEqual) {
                double dist = Distance3DOp.distance(cell.getDuality().getPoint(), qPoint);
                double dz = cell.getGeometry2D().getCoordinate().z - c.z;
                if(Math.abs(dz) < 3) {
                    if(dist < minValue) {
                        minValue = dist;
                        min = cell;
                    }
                }
            }
            result = min;
            return result;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "SpaceLayer ["
        		+ "\n nodes=" + nodes 
        		+ "\n edges=" + edges 
        		+ "]";
    }
}