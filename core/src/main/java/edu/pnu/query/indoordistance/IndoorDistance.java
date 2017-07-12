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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rx.util.Pair;
import com.google.common.collect.MinMaxPriorityQueue;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.pnu.stemlab.model.geometry.Coordinate;

/**
 * @author hgryoo
 *
 */
public class IndoorDistance {
    
    DistanceGraph distanceGraph;
    
    public double getIntraDoortoDoorDistance(Partition p, Door i, Door j) {
        double dist = DistanceGraph.INF;
        if(i.equals(j)) {
            List<Door> connected = distanceGraph.getConnectedDoors(p);
            if(connected.contains(i) && connected.contains(j)) {
                dist = -1;
            }
        } else if(distanceGraph.getEnterableDoors(p).contains(i) && distanceGraph.getLeavableDoors(p).contains(j)) {
            dist = 0;
        }
        return dist;
    }
    
    public double getPointtoPointDistance(IndoorPosition so, IndoorPosition dest) {
        Partition sp = distanceGraph.getHostPartition(so);
        Partition tp = distanceGraph.getHostPartition(so);
        double dist = DistanceGraph.INF;
        
        for(Door ds : distanceGraph.getLeavableDoors(sp)) {
            double dist1 = getShortestIntraPartitionDist(so, ds);
            for(Door dt : distanceGraph.getEnterableDoors(tp)) {
                double dist2 = getShortestIntraPartitionDist(dest, dt);
                double d2d = getDoortoDoorDistance(ds, dt);
                if(dist > dist1 + d2d + dist2) {
                    dist = dist1 + d2d + dist2;
                }
            }
        }
        
        return dist;
    }
    
    public double getDoortoDoorDistance(Door ds, Door dt) {
        MinMaxPriorityQueue< Pair<String, Double> > h = MinMaxPriorityQueue.orderedBy(null)
                .maximumSize(1000)
                .create();
                
        double[] dist = new double[distanceGraph.numDoors()];
        String[] prev = new String[distanceGraph.numDoors()];
        boolean[] visited = new boolean[distanceGraph.numDoors()];
        Map<String, Integer> idx = new HashMap<String, Integer>();
        
        int i = 0;
        
        Iterator<Door> it = distanceGraph.getDoors();
        while(it.hasNext()) {
            Door d = it.next();
            String dId = d.getId();
            if(dId.equalsIgnoreCase(ds.getId())) {
                dist[i] = 0.0d;
            } else {
                dist[i] = Double.MAX_VALUE;
            }
            visited[i] = false;
            prev[i] = null;
            idx.put(dId, i);
            i++;
            
            Pair<String,Double> pair = Pair.create(dId, dist[i]);
            h.add(pair);
        }
        
        double result = DistanceGraph.INF;
        while(!h.isEmpty()) {
            Pair<String,Double> di = h.poll();
            if(di.a().equalsIgnoreCase(dt.getId())) {
                result = dist[ idx.get(di.a()) ];
                break;
            }
            
            visited[ idx.get(di.a()) ] = true;
            List<Partition> parts = distanceGraph.getLeavablePartition(distanceGraph.getDoorById(di.a()));
            for(Partition p : parts) {
                for(Door dj : distanceGraph.getLeavableDoors(p)) {
                    double diDist = dist[ idx.get(di.a())];
                    double djDist = dist[ idx.get(dj.getId())];
                    double intraD2DDist = getIntraDoortoDoorDistance(p, distanceGraph.getDoorById(di.a()), dj);
                    
                    if(diDist + intraD2DDist < djDist) {
                        djDist = diDist + intraD2DDist;
                        //replace dj’s element in H by dj, dist[dj]
                        prev[ idx.get(dj.getId()) ]  = di.a();
                    }
                }
            }
        }
        
        return result;
    }

    public double getShortestIntraPartitionDist(IndoorPosition ip, Door d) {
        Point p = (Point) ip.getGeometry();
        Coordinate pC = (Coordinate) p.getCoordinate();
        
        LineString dGeom = (LineString) d.getGeometry();
        Coordinate dCen = (Coordinate) dGeom.getCentroid().getCoordinate();
        
        return pC.distance(dCen);
    }
    
}
