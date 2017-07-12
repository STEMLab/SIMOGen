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

import java.util.Iterator;
import java.util.List;

/**
 * @author hgryoo
 *
 */
public interface DistanceGraph {
    
    public static final double INF = Double.MAX_VALUE;

    public Door getDoorById(String id);
    
    public Partition getPartitionById(String id);
    
    public double getIntraDoortoDoorDistance(Partition p, Door i, Door j);
    
    public double getIntraLongestDistance(Door d, Partition p);
      
    public Partition getHostPartition(IndoorPosition so);

    
    // Topology
    
    public List<Door> getEnterableDoors(Partition p) ;
    
    public List<Door> getLeavableDoors(Partition p);
    
    public List<Door> getConnectedDoors(Partition p);
    
    public List<Partition> getEnterablePartition(Door d) ;
    
    public List<Partition> getLeavablePartition(Door d);
    
    public int numDoors();
    
    public int numPartitions();
    
    public Iterator<Door> getDoors();
    
    public Iterator<Partition> getPartitions();
}
