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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import edu.pnu.model.SpaceBuilder;
import edu.pnu.model.SpaceLayer;

/**
 * @author hgryoo
 *
 */
public class SimpleIndoorGMLImporter {

    private SpaceBuilder builder;
    
    public SimpleIndoorGMLImporter(String url) throws ParserConfigurationException, SAXException, IOException {
        this(new File(url));
    }
    
    public SimpleIndoorGMLImporter(File file)  throws ParserConfigurationException, SAXException, IOException  {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SimpleIndoorGMLHandlerNG handler = new SimpleIndoorGMLHandlerNG();
        parser.parse(file, handler);
        builder = handler.getSpaceBuilder();
    }
    
    public SpaceLayer getSpaceLayer() {
        return builder.buildSpaceLayer();
    }
    
    public static void main(String[] args) {
        try {
            SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/SAMPLE_DATA_LWM_2D.gml");
            SpaceLayer l = importer.getSpaceLayer();
            System.out.println(l);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}