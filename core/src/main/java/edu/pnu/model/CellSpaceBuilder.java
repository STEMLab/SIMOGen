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
package edu.pnu.model;

import java.util.Map;
import org.apache.log4j.Logger;
import edu.pnu.model.primal.CellSpace;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author hgryoo
 *
 */
public class CellSpaceBuilder {
    private static final Logger LOGGER = Logger.getLogger(CellSpaceBuilder.class.getName());
    
    private String id;
    
    private Polygon polygon;
    
    private Map<Object, Object> userData;
    
    public void reset() {
        id = null;
        polygon = null;
        userData = null;
    }
    
    public void setGeometry2D(Polygon p) {
        this.polygon = p;
        //TODO : make box
    }

    public CellSpace build() throws IllegalArgumentException {
        if(id == null) {
            LOGGER.error("Id is missing");
            throw new IllegalArgumentException();
        }

        return new CellSpace(id);
    }
}
