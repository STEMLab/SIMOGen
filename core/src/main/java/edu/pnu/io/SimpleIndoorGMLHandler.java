/*
 * Indoor Moving Objects Generator
 * Copyright (c) 2016 Pusan National University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package edu.pnu.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import edu.pnu.model.SpaceBuilder;
import edu.pnu.model.dual.State;
import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class SimpleIndoorGMLHandler extends DefaultHandler {
    private static final Logger LOGGER = Logger.getLogger(SimpleIndoorGMLHandler.class);
    
    private static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    private static final String GML_NS = "http://www.opengis.net/gml/3.2";
    private static final String INDOOR_CORE_NS = "http://www.opengis.net/indoorgml/1.0/core"; 

    private boolean pos;
    private boolean desc;
    
    private String id;
    
    private Point p;
    private LineString l;
    private Polygon poly;
    
    private List<Coordinate> coords = new ArrayList<Coordinate>();
    private List<String> neighbors = new ArrayList<String>();
    private String duality;
    
    private Map<Object, Object> userData = new HashMap<Object, Object>();
    
    private GeometryFactory geomFac = new GeometryFactory();
    private SpaceBuilder builder = new SpaceBuilder();
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if(qName.contains("State")) {
            id = attributes.getValue("gml:id");
        } else if(qName.contains("Transition")) {
            id = attributes.getValue("gml:id");
        } else if(qName.contains("CellSpace")) {
            id = attributes.getValue("gml:id");
        } else if(qName.contains("connects")) {
            neighbors.add(attributes.getValue("xlink:href").replaceAll("#", ""));
        } else if(qName.contains("duality")) {
            duality = attributes.getValue("xlink:href").replaceAll("#", "");
        } else if(qName.contains("pos")) {
            pos = true;
        } else if(qName.contains("description")) {
            desc = true;
        } else if(qName.contains("IndoorFeatures")) {
            attributes.getValue("xmlns");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.contains("State")) {
            State s = new State(id, p);
            
            CellSpace cell = builder.getCellSpace(duality);
            cell.setDuality(s);
            s.setDuality(cell);
            
            builder.addState(s);
            neighbors.clear();
        } else if(qName.contains("Transition")) {
            //Transition t = new Transition(id, l);
            
            String aId = neighbors.get(0);
            String bId = neighbors.get(1);
            
            State first = builder.getState(aId);
            State second = builder.getState(bId);
            
            if(first == null || second == null) {
                LOGGER.fatal("transition has null neighbor =>\n" + neighbors);
            }
            
            builder.createTransition(id, first, second, l);
            neighbors.clear();
            
        } else if(qName.contains("CellSpace")) {
            CellSpace c = new CellSpace(id, poly);
            builder.addCellSpace(c);
            
        } else if(qName.contains("Point")) {
            p = geomFac.createPoint(coords.get(0));
            coords.clear();
            
        } else if(qName.contains("LineString")) {
            Coordinate[] cs = new Coordinate[coords.size()];
            for(int i = 0; i < cs.length; i++) {
                cs[i] = coords.get(i);
            }
            l = geomFac.createLineString(cs);
            coords.clear();
            
        } else if(qName.contains("Polygon")) {
            Coordinate[] cs = new Coordinate[coords.size()];
            for(int i = 0; i < cs.length; i++) {
                cs[i] = coords.get(i);
            }
            poly = geomFac.createPolygon(cs);
            coords.clear();  
            
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(pos) {
            String ps = new String(ch, start, length).trim();
            String[] cs = ps.split(" ");
            Coordinate coord = new Coordinate(
                    Double.parseDouble(cs[0]),
                    Double.parseDouble(cs[1]),
                    Double.parseDouble(cs[2]));
            coords.add(coord);
            pos = false;
        }
        
        if(desc) {
            String description = new String(ch, start, length).trim();
            String[] ds = description.split(":");
            
            userData.clear();
            for(String s : ds) {
                String[] key_value = s.split("=");
                String key = key_value[0];
                String value = key_value[1];
                userData.put(key, value);
            }
            desc = false;
        }
    }
    
    public SpaceBuilder getSpaceBuilder() {
        return builder;
    }
}
