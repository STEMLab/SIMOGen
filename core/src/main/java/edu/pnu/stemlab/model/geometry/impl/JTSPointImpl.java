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

import com.vividsolutions.jts.geom.Point;

import edu.pnu.stemlab.model.geometry.Coordinate;
import edu.pnu.stemlab.model.geometry.Envelope;
import edu.pnu.stemlab.model.geometry.Geometry;

/**
 * @author hgryoo
 *
 */
public class JTSPointImpl extends JTSGeometryImpl implements edu.pnu.stemlab.model.geometry.Point {

    public JTSPointImpl(com.vividsolutions.jts.geom.Point point) {
        super(point);
    }
    
    protected com.vividsolutions.jts.geom.Point getJTSGeometry() {
        return (Point) super.getJTSGeometry();
    }
    
    public int getDimension() {
        return getJTSGeometry().getDimension();
    }

    public int getCoordinateDimension() {
        return getJTSGeometry().getCoordinateSequence().getDimension();
    }

    public Coordinate getCoordinate() {
        
    }

    public double getX() {
        return getJTSGeometry().getX();
    }

    public double getY() {
        return getJTSGeometry().getY();
    }

    public double getZ() {
        return getJTSGeometry().getCoordinate().z;
    }

    @Override
    public boolean isSimple() {
        return getJTSGeometry().isSimple();
    }

    @Override
    public boolean isCycle() {
        return true;
    }

    @Override
    public boolean distance(Geometry geometry) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Envelope getEnvelope() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point getCentroid() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point getRepresentativePoint() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Geometry getBoundary() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Geometry getConvexHull() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Geometry getBuffer(double distance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Geometry clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

}
