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
package core;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class CellSpaceIndexingTest {
   /* 
    private SpaceLayer layer;
    
    @Before
    public void setUp() throws Exception {
        SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/test.xml");
        layer = importer.getSpaceLayer();
    }
    
    @Test
    public void testStateInCellSpace() {
        List<State> states = layer.getNodes();
        
        for(State s : states) {
            CellSpace duality = s.getDuality();
            Coordinate qc = s.getPoint().getCoordinate();
            CellSpace queryCell = layer.getCellSpace(qc);
            
            if(queryCell == null || !duality.equals(queryCell)) {
                fail();
            }
        }
    }
    
    @Test
    public void test() {
        try {
            Class.forName("org.postgresql.Driver"); 
            String url = "jdbc:postgresql://localhost:5432/sfcgal"; 
            Connection conn = DriverManager.getConnection(url, "postgres", "csh9264");
            
            SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/test.xml");
            SpaceLayer layer = importer.getSpaceLayer();
            layer.buildIndex();
            
            int a = 0;
            List<State> states = layer.getNodes();
            
            for(State s : states) {
                CellSpace duality = s.getDuality();
                Coordinate qc = s.getPoint().getCoordinate();
                CellSpace queryCell = layer.getCellSpace(qc);
                
                if(!duality.equals(queryCell)) {
                    fail();
                }
            }
            
            List<CellSpace> cells = layer.getCells();
            
            for(CellSpace cell : cells) {
                Polygon p = cell.getGeometry2D();
                Coordinate[] coords = p.getCoordinates();
            
                Statement s = conn.createStatement();
                String sql = "SELECT ST_StraightSkeleton(ST_GeomFromEWKT('POLYGON Z(( ";
                
                for(Coordinate c : coords) {
                    sql += c.x + " " + c.y + " " + c.z + ",";
                }
                
                sql = sql.substring(0, sql.length() - 1);
                sql += "))'::geometry));";
            
                double z = p.getCentroid().getCoordinate().z;
                ResultSet r = s.executeQuery(sql);
                while(r.next()) {
                    Object o = r.getObject(1);
                    
                    if(o instanceof MultiLineString) {
                        MultiLineString ms = (MultiLineString) o;
                        for(LineString line : ms.getLines()) {
                            Point[] points = line.getPoints();
                            for(int i = 0; i < points.length - 1; i++) {
                                
                            }
                        }
                    }
                    // System.out.println();
                }
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

}
