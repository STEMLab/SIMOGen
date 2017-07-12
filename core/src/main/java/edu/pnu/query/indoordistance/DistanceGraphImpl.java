/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package edu.pnu.query.indoordistance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import edu.pnu.util.GeometryUtil;

/**
 * @author hgryoo
 *
 */
public class DistanceGraphImpl implements DistanceGraph {

    private Map<String, Partition> partitions;
    private Map<String, Door> doors;
    
    public double getIntraDoortoDoorDistance(Partition p, Door i, Door j) {
        double dist = INF;
        if(i.equals(j)) {
            List<Door> connected = getConnectedDoors(p);
            if(connected.contains(i) && connected.contains(j)) {
                dist = calculateLineofSight(p, i, j);
            }
        } else if(getEnterableDoors(p).contains(i) && getLeavableDoors(p).contains(j)) {
            dist = 0;
        }
        return dist;
    }

    private double calculateLineofSight(Partition p, Door i, Door j) {
        LineString doorGeom1 = (LineString) i.getGeometry();
        Coordinate centerP1 = doorGeom1.getCentroid().getCoordinate();
        
        LineString doorGeom2 = (LineString) j.getGeometry();
        Coordinate centerP2 = doorGeom2.getCentroid().getCoordinate();
        
        return centerP1.distance(centerP2);
    }
    
    public double getIntraLongestDistance(Door d, Partition p) {
        double dist = INF;
        if(p.equals(getEnterablePartition(d))) {
            dist = calculateLongestDistance(d, p);
        }
        return dist;
    }
    
    private double calculateLongestDistance(Door d, Partition p) {
        //HACK : the geometry of partition is assumed as a convex.
        Polygon partitionGeom = (Polygon) p.getFloor();
        Coordinate[] cs = partitionGeom.getCoordinates();
        
        LineString doorGeom = (LineString) d.getGeometry();
        Coordinate centerP = doorGeom.getCentroid().getCoordinate();
        
        double max = Double.MIN_VALUE;
        for(Coordinate c : cs) {
            double dist = centerP.distance(c);
            if(dist > max) {
                max = dist;
            }
        }
        return max;
    }

    public Partition getHostPartition(IndoorPosition ip) {
        List<Partition> candidates = new ArrayList<Partition>();
        for(Partition p : partitions.values()) {
            if(calculatePositioninPolygon(p, ip)) {
                candidates.add(p);
            }
        }
        //TODO
        return candidates.get(0);
    }
    
    private boolean calculatePositioninPolygon(Partition p, IndoorPosition ip) {
        Point point = (Point) ip.getGeometry();
        Polygon polygon = (Polygon) p.getGeometry();
        
        return GeometryUtil.pointInTriangle(polygon, point.getCoordinate());
    }
    
    public List<Door> getEnterableDoors(Partition p) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Door> getLeavableDoors(Partition p) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Door> getConnectedDoors(Partition p) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Partition> getEnterablePartition(Door d) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Partition> getLeavablePartition(Door d) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int numDoors() {
        return doors.size();
    }
    
    public int numPartitions() {
        return partitions.size();
    }
    
    public Iterator<Door> getDoors() {
        return doors.values().iterator();
    }
    

    public Iterator<Partition> getPartitions() {
        return partitions.values().iterator();
    }

    public Door getDoorById(String id) {
        if(doors.containsKey(id)) {
            return doors.get(id);
        }
        return null;
    }

    public Partition getPartitionById(String id) {
        if(partitions.containsKey(id)) {
            return partitions.get(id);
        }
        return null;
    }

}
