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

import edu.pnu.stemlab.model.geometry.Coordinate;

/**
 * @author hgryoo
 *
 */
public class JTSCoordinateImpl implements Coordinate {
    
    private com.vividsolutions.jts.geom.Coordinate jtsGeom;
    
    public JTSCoordinateImpl(com.vividsolutions.jts.geom.Coordinate jtsCoord) {
        jtsGeom = jtsCoord;
    }
    
    public com.vividsolutions.jts.geom.Coordinate getJTSCoordinate() {
        return jtsGeom;
    }
    
    public double distance(Coordinate p) {
        
    }

    public int getDimension() {
        // TODO Auto-generated method stub
        return 0;
    }

    public double[] getCoordinate() {
        // TODO Auto-generated method stub
        return null;
    }

    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Double getX() {
        if(com.vividsolutions.jts.geom.Coordinate.NULL_ORDINATE != jtsGeom.x) {
            return jtsGeom.x;
        }
        return null;
    }

    public Double getY() {
        if(com.vividsolutions.jts.geom.Coordinate.NULL_ORDINATE != jtsGeom.y) {
            return jtsGeom.y;
        }
        return null;
    }

    public Double getZ() {
        if(com.vividsolutions.jts.geom.Coordinate.NULL_ORDINATE != jtsGeom.z) {
            return jtsGeom.z;
        }
        return null;
    }

}
