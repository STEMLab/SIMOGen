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

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author hgryoo
 *
 */
public class PartitionImpl implements Partition {
    
    private String id;
    private Polygon geom;
    private List<Door> doors;
    
    public PartitionImpl(String id, Polygon geom, List<Door> ds) {
        this.id = id;
        this.geom = geom;
        this.doors = ds;
    }
    
    public boolean is3D() {
        return false;
    }

    public String getId() {
        return id;
    }
    
    public Object getGeometry() {
        return geom;
    }

    public Object getFloor() {
        return geom;
    }

    public List<Door> getAdjacencentDoors() {
        return doors;
    }

}
