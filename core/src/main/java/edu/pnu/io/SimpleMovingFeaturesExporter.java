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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.model.History;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.primal.CellSpace;


/**
 * @author hgryoo
 * Simple exporter for Moving Features standards.
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
 * <mf:MovingFeatures xmlns:mf="http://schemas.opengis.net/mfcore/1.0"
 *      xmlns:xlink="http://www.w3.org/1999/xlink"
 *      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
 *      xmlns:gml="http://www.opengis.net/gml/3.2"
 *      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *      xsi:schemaLocation="http://schemas.opengis.net/mf-core/1.0 
 *      moving_features_gml_app.xsd"
 *      gml:id="MFC_0001">
 *      <mf:foliation>
 *              <mf:LinearTrajectory gml:id="LT0001" mfIdRef="a" start="10" end="150">
 *                      <gml:posList>11.0 2.0 12.0 3.0</gml:posList>
 *              </mf:LinearTrajectory>
 *              <mf:LinearTrajectory gml:id="LT0002" mfIdRef="b" start="10" end="190">
 *                      <gml:posList>10.0 2.0 11.0 3.0</gml:posList>
 *              </mf:LinearTrajectory>
 *              <mf:LinearTrajectory gml:id="LT0003" mfIdRef="a" start="150" end="190">
 *                      <gml:posList>12.0 3.0 10.0 3.0</gml:posList>
 *              </mf:LinearTrajectory>
 *      </mf:foliation>
 * </mf:MovingFeatures>
 *  
 */

public class SimpleMovingFeaturesExporter {
    private String id; 
    private Map<String, List<History>> trajectory = new HashMap<String, List<History>> ();
    
    public SimpleMovingFeaturesExporter(String id) {
        this.id = id;
    }
    
    public void addHistory(String mvId, List<History> history) {
        if(trajectory.containsKey(mvId)) {
            trajectory.remove(mvId);
        }
        trajectory.put(mvId, history);
    }
    
    public void bufferedExport(String path) {
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path)));
            
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +
                    "<mf:MovingFeatures xmlns:mf=\"http://schemas.opengis.net/mfcore/1.0\"" + "\n" +
                    "xmlns:xlink=\"http://www.w3.org/1999/xlink\"" + "\n" +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + "\n" +
                    "xmlns:gml=\"http://www.opengis.net/gml/3.2\"" + "\n" +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n" +
                    "xsi:schemaLocation=\"http://schemas.opengis.net/mf-core/1.0" + "\n" + 
                    "file:///C:/Users/stem/Desktop/MovinObjectIn_Indoor/moving_features_gml_app.xsd\"" + "\n" +
                    "gml:id=\"" + id + "\">");
            writer.newLine();
            
            writer.write("<mf:foliation>");
            writer.newLine();
            
            for( Map.Entry<String, List<History>> elem : trajectory.entrySet() ){
                
                String mvId = elem.getKey();
                List<History> history = elem.getValue();
                
                History prev = history.get(0);
                History next = null;
                for(int i = 1; i < history.size(); i++) {
                    
                    next = history.get(i);
                    
                    writer.write("<mf:LinearTrajectory "
                            + "gml:id=\"" + UUID.randomUUID().toString() + 
                            "\" mfIdRef=\"" + mvId +
                            "\" start=\"" + prev.getTime() + 
                            "\" end=\"" + next.getTime() + "\">");
                    writer.newLine();
                    
                    writer.write(
                            "<gml:posList>" +
                                    SimpleIOUtils.coordinateToString(prev.getCoord()) + " " + 
                                    SimpleIOUtils.coordinateToString(next.getCoord()) +
                            "</gml:posList>"
                            );
                    writer.newLine();
                    
                    writer.write("</mf:LinearTrajectory>");
                    writer.newLine();

                    prev = next;
                }
            }
            writer.write("</mf:foliation>" + "\n");
            writer.newLine();
            writer.write("</mf:MovingFeatures>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    public String export() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +
                "<mf:MovingFeatures xmlns:mf=\"http://schemas.opengis.net/mfcore/1.0\"" + "\n" +
                "xmlns:xlink=\"http://www.w3.org/1999/xlink\"" + "\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + "\n" +
                "xmlns:gml=\"http://www.opengis.net/gml/3.2\"" + "\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n" +
                "xsi:schemaLocation=\"http://schemas.opengis.net/mf-core/1.0" + "\n" + 
                "file:///C:/Users/stem/Desktop/MovinObjectIn_Indoor/moving_features_gml_app.xsd\"" + "\n" +
                "gml:id=\"" + id + "\">" + "\n");
        
        builder.append("<mf:foliation>" + "\n");
        
        for( Map.Entry<String, List<History>> elem : trajectory.entrySet() ){
            
            String mvId = elem.getKey();
            List<History> history = elem.getValue();
            
            History prev = history.get(0);
            History next = null;
            for(int i = 1; i < history.size(); i++) {
                
                next = history.get(i);
                
                builder.append("<mf:LinearTrajectory "
                        + "gml:id=\"" + UUID.randomUUID().toString() + 
                        "\" mfIdRef=\"" + mvId +
                        "\" start=\"" + prev.getTime() + 
                        "\" end=\"" + next.getTime() + "\">\n");
                
                builder.append(
                        "<gml:posList>" +
                                SimpleIOUtils.coordinateToString(prev.getCoord()) + " " + 
                                SimpleIOUtils.coordinateToString(next.getCoord()) +
                        "</gml:posList>"
                        );
                
                builder.append("</mf:LinearTrajectory>\n");
                prev = next;
            }
        }
        builder.append("</mf:foliation>" + "\n");
        builder.append("</mf:MovingFeatures>");
        
        return builder.toString();
    }

public void exportPostGIS(SpaceLayer sl) throws IOException, SQLException {
	 Statement st = null;
	 Connection cx = null; 

	 
   
         // connect to template1 instead
         String url = "jdbc:postgresql" + "://" + "localhost" + ":" + "5432" + "/zone";

         cx = DriverManager.getConnection(url, "postgres", "gis");
         
         
         for( Map.Entry<String, List<History>> elem : trajectory.entrySet() ){
        	  try {  
        		  String sql = "insert into lotte values(";
        		  st = cx.createStatement();
             String mvId = elem.getKey();
             List<History> history = elem.getValue();
             
             History prev = null;
             History next = null;
             for(int i = 0; i < history.size() - 1; i++) {
            	 
                 prev = history.get(i);
                 next = history.get(i + 1);
                 
                 CellSpace prevc = sl.getCellSpace(prev.getCoord());
                 CellSpace nextc = sl.getCellSpace(next.getCoord());
                 String prevcell = "";
                 String nextcell = "c";
                 if(prevc != null) {
                	 prevcell = prevc.getId();
                 }
                 if(nextc != null) {
                	 nextcell = nextc.getId();
                 }
                 
                 
                 if(i == 0) {
                	 sql += "'" + mvId + "',";
                	 sql += "'" + prevcell + "',";
                	 sql += prev.getTime() + ",";
                	
                 }if(!prevcell.equalsIgnoreCase(nextcell)) {
                	 sql += next.getTime() + ");";
                     sql += "insert into lotte values(";
                     sql += "'" + mvId + "',";
                	 sql += "'" + nextcell + "',";
                	 sql +=  next.getTime() + ",";
                 }
                 prev = next;
             }
             
        	 sql += next.getTime() + ");";
        	 System.out.println(sql);
        	 //st.addBatch(sql);
             st.executeUpdate(sql);
                
        	  } catch (SQLException e) {
        		  System.out.println(e.getMessage());
        		  continue;
        	         //throw new IOException("Failed to create the target database", e);
        	     }
         }
         
         //st.executeBatch();
      //finally {
    	 st.close();
    	 cx.close();
     //}
        
        
    }
}
