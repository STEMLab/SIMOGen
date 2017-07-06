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
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rx.util.Pair;
import com.google.common.collect.MinMaxPriorityQueue;

import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class IndoorDistance {
    
    public static final double INF = Double.MAX_VALUE;
    
    private Map<String, Partition> partitions;
    private Map<String, Door> doors;
    
    
    public double getIntraDoortoDoorDistance(Partition p, Door i, Door j) {
        return INF;
    }
    
    public double getPointtoPointDistance(IndoorPosition so, IndoorPosition dest) {
        Partition sp = getHostPartition(so);
        Partition tp = getHostPartition(so);
        double dist = INF;
        
        for(Door ds : getLeavableDoors(sp)) {
            double dist1 = getShortestIntraPartitionDist(so, ds);
            for(Door dt : getEnterableDoors(tp)) {
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
                
        double[] dist = new double[doors.size()];
        String[] prev = new String[doors.size()];
        boolean[] visited = new boolean[doors.size()];
        Map<String, Integer> idx = new HashMap<String, Integer>();
        
        int i = 0;
        for(String dId : doors.keySet()) {
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
        
        while(!h.isEmpty()) {
            Pair<String,Double> di = h.poll();
            if(di.a().equalsIgnoreCase(dt.getId())) {
                return dist[ idx.get(di.a()) ];
            }
            
            visited[ idx.get(di.a()) ] = true;
            List<Partition> parts = getLeavablePartition(doors.get(di.a()));
            for(Partition p : parts) {
                for(Door dj : getLeavableDoors(p)) {
                    double diDist = dist[ idx.get(di.a())];
                    double djDist = dist[ idx.get(dj.getId())];
                    double intraD2DDist = getIntraDoortoDoorDistance(p, doors.get(di.a()), dj);
                    
                    if(diDist + intraD2DDist < djDist) {
                        djDist = diDist + intraD2DDist;
                        //replace dj’s element in H by dj, dist[dj]
                        prev[ idx.get(dj.getId()) ]  = di.a();
                    }
                }
            }
        }
        
        return INF;
    }

    public double getShortestIntraPartitionDist(IndoorPosition p, Door d) {
        
        return INF;
    }
    
    private Partition getHostPartition(IndoorPosition so) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Door> getEnterableDoors(Partition p) {
        return null;
    }
    
    public List<Door> getLeavableDoors(Partition p) {
        return null;
    }
    
    public List<Partition> getEnterablePartition(Door d) {
        return null;
    }
    
    public List<Partition> getLeavablePartition(Door d) {
        return null;
    }
    
}
