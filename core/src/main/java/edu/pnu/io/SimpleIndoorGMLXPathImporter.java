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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.pnu.model.SpaceBuilder;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;

/**
 * @author hgryoo
 *
 */
public class SimpleIndoorGMLXPathImporter {
    
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;
    
    private SpaceBuilder builder;
    
    public SimpleIndoorGMLXPathImporter(String url) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(url));
        
        File file = new File(url);
        System.out.println(file.getAbsolutePath());
        
        doc = db.parse(file);
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        builder = new SpaceBuilder();
        GeometryFactory geomFac = new GeometryFactory();
        
        
        NodeList states = (NodeList) xpath.evaluate(
                "//IndoorFeatures/MultiLayeredGraph/spaceLayers/spaceLayerMember/SpaceLayer/nodes/stateMember/State",
                doc,
                XPathConstants.NODESET);
        for(int i = 0; i < states.getLength(); i++) {
            Node sNode = states.item(i);
            if(sNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sElem = (Element) sNode;
                
                String stateId = sElem.getAttribute("id");
                Point p = null;
                
                String pos = (String) xpath.evaluate("//geometry/Point/pos", sNode, XPathConstants.STRING);
                String[] coords = pos.split(" ");
                p = geomFac.createPoint(new Coordinate(
                        Double.parseDouble(coords[0]),
                        Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])));
                
                State s = new State(stateId, p);
                builder.addState(s);
            }
        }
        
        NodeList transitions = (NodeList) xpath.evaluate(
                "//IndoorFeatures/MultiLayeredGraph/spaceLayers/spaceLayerMember/SpaceLayer/edges/transitionMember/Transition",
                doc,
                XPathConstants.NODESET);
        for(int i = 0; i < transitions.getLength(); i++) {
            Node tNode = transitions.item(i);
            if(tNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sElem = (Element) tNode;
                
                String transitionid = sElem.getAttribute("id");
                LineString l = null;
                
                NodeList posList = (NodeList) xpath.evaluate("//geometry/LineString/pos", tNode, XPathConstants.NODESET);
                Coordinate[] coordList = new Coordinate[posList.getLength()];
                for(int j = 0; j < coordList.length; j++) {
                    String[] coords = posList.item(j).getNodeValue().split(" ");
                    coordList[j] = new Coordinate(
                            Double.parseDouble(coords[0]),
                            Double.parseDouble(coords[1]),
                            Double.parseDouble(coords[2]));
                }
                l = geomFac.createLineString(coordList);                
                Transition t = new Transition(transitionid, l);
                
                NodeList connects = (NodeList) xpath.evaluate("//connects", tNode, XPathConstants.NODESET);
                String aId = connects.item(0).getNodeValue();
                String bId = connects.item(1).getNodeValue();
                
                State first = builder.getState(aId);
                State second = builder.getState(bId);
                
                t.setState(first, second);
                first.addConnects(t);
                second.addConnects(t);
                builder.addTransition(t);
            }
        }
    }
    
    public SpaceLayer getSpaceLayer() {
        return builder.buildSpaceLayer();
    }
    
    public static void main(String[] args) {
        try {
            SimpleIndoorGMLXPathImporter importer = new SimpleIndoorGMLXPathImporter("target/LWM_IGML.gml");
            
            SpaceLayer l = importer.getSpaceLayer();
            
            System.out.println(l);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}