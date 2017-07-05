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
package edu.pnu.stemlab.model.geometry.impl;

import java.util.ArrayList;
import java.util.List;

import edu.pnu.stemlab.model.geometry.Coordinate;

/**
 * @author hgryoo
 *
 */
public class JTSGeometryConvertor {
    
    public static com.vividsolutions.jts.geom.Coordinate convertCoordinate(Coordinate c) {
        com.vividsolutions.jts.geom.Coordinate jtsCoord = new com.vividsolutions.jts.geom.Coordinate(
                c.getX(), c.getY(), c.getZ());
        return jtsCoord;
    }
    
    public static List<com.vividsolutions.jts.geom.Coordinate> convertCoordinates(List<Coordinate> cs) {
        List<com.vividsolutions.jts.geom.Coordinate> jtsCs = new ArrayList<com.vividsolutions.jts.geom.Coordinate>();
        for(Coordinate c : cs) {
            jtsCs.add(convertCoordinate(c));
        }
        return jtsCs;
    }
    
}
