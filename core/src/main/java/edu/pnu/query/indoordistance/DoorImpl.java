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

import com.vividsolutions.jts.geom.LineString;

/**
 * @author hgryoo
 *
 */
public class DoorImpl implements Door {
    
    private String id;
    private LineString geom;
    private List<Partition> partitions;
    
    public DoorImpl(String id, LineString geom, List<Partition> ps) {
        this.id = id;
        this.geom = geom;
        this.partitions = ps;
    }
    
    public String getId() {
        return id;
    }

    public Object getGeometry() {
        return geom;
    }

}
