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

import java.util.List;

import edu.pnu.stemlab.model.geometry.Coordinate;
import edu.pnu.stemlab.model.geometry.Curve;
import edu.pnu.stemlab.model.geometry.Envelope;
import edu.pnu.stemlab.model.geometry.GeometryFactory;
import edu.pnu.stemlab.model.geometry.Point;
import edu.pnu.stemlab.model.geometry.Polygon;
import edu.pnu.stemlab.model.geometry.Ring;
import edu.pnu.stemlab.model.geometry.Shell;
import edu.pnu.stemlab.model.geometry.Solid;

/**
 * @author hgryoo
 *
 */
public class JTSGeometryFactoryImpl implements GeometryFactory {
    
    private com.vividsolutions.jts.geom.GeometryFactory jtsGeomFac;
    
    public JTSGeometryFactoryImpl() {
        jtsGeomFac = new com.vividsolutions.jts.geom.GeometryFactory();
    }
    
    public Envelope createEnvelope(Coordinate lower, Coordinate upper) {
        
        
        return null;
    }

    public Point createPoint(Coordinate c) {
        com.vividsolutions.jts.geom.Coordinate jtsCoord = JTSGeometryConvertor.convertCoordinate(c);
        com.vividsolutions.jts.geom.Point jtsP = jtsGeomFac.createPoint(jtsCoord);
        return new JTSPointImpl(jtsP);
    }

    public Curve createCurve(List<Coordinate> coords) {
        List<com.vividsolutions.jts.geom.Coordinate> jtsCoords = JTSGeometryConvertor
                .convertCoordinates(coords);
        com.vividsolutions.jts.geom.Coordinate[] cArray = jtsCoords
                .toArray(new com.vividsolutions.jts.geom.Coordinate[jtsCoords.size()]);
        com.vividsolutions.jts.geom.LineString l = jtsGeomFac.createLineString(cArray);
        return null;
    }

    public Ring createRing(Curve c) {
        // TODO Auto-generated method stub
        return null;
    }

    public Polygon createPolygon(Ring exterior, List<Ring> interiors) {
        // TODO Auto-generated method stub
        return null;
    }

    public Shell createShell(List<Polygon> polygons) {
        // TODO Auto-generated method stub
        return null;
    }

    public Solid createSolid(Shell exterior, List<Shell> interiors) {
        // TODO Auto-generated method stub
        return null;
    }

}
