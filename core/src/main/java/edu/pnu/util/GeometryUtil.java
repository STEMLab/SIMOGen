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
package edu.pnu.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.vividsolutions.jts.algorithm.CGAlgorithms3D;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Triangle;
import com.vividsolutions.jts.math.Vector3D;

import edu.pnu.model.primal.CellSpace;

/**
 * @author hgryoo
 *
 */
public class GeometryUtil {
    private static final int zOrdinate = Coordinate.Z;
    
    private static GeometryFactory factory = null;
    
    public static double distance(Coordinate a, Coordinate b) {
        return CGAlgorithms3D.distance(a, b);
    }
    
    public static Coordinate fromTo(Coordinate from, Coordinate to, double dist) {
        Vector3D fromVec = Vector3D.create(from);
        Vector3D toVec = Vector3D.create(to);
        
        double mn = distance(from, to);
        double n = mn - dist;
        
        Vector3D result = Vector3D.create(
                (dist * toVec.getX() + n * fromVec.getX()) / mn,
                (dist * toVec.getY() + n * fromVec.getY()) / mn,
                (dist * toVec.getZ() + n * fromVec.getZ()) / mn);
        
        return new Coordinate(result.getX(), result.getY(), result.getZ());
    }
    
    public static GeometryFactory getGeometryFactory() {
        if(factory == null) {
            factory = new GeometryFactory();
        }
        return factory;
    }
    
    public boolean PointInPolygon3D(Coordinate c, Polygon p) {
        return false;
    }
    
    public static Vector3D getNoiseVector(final double sigma, final int count) {
        Random rand = new Random();
        
        double offsetX = 0;
        double offsetY = 0;
        
        for(int i = 0; i < count; i++) {
                offsetX += Math.cos(Math.toRadians(rand.nextInt(360))) * rand.nextGaussian() * sigma;
                offsetY += Math.sin(Math.toRadians(rand.nextInt(360))) * rand.nextGaussian() * sigma;
        }
        
        offsetX /= count;
        offsetY /= count;
        
        return new Vector3D(offsetX, offsetY, 0);
    }
    
    public static Coordinate getNoisedCoordinate(Coordinate origin, Vector3D noise) {
        Coordinate noisedCoordinate = new Coordinate
                (origin.x + noise.getX(), origin.y + noise.getY() , origin.z + noise.getZ());
        return noisedCoordinate;
    }
    
    public static Coordinate getNoisedCoordinate(Coordinate origin, final double sigma, final int count) {
        Vector3D noise = getNoiseVector(sigma, count);
        Coordinate noisedCoordinate = new Coordinate
                (origin.x + noise.getX(), origin.y + noise.getY() , origin.z + noise.getZ());
        return noisedCoordinate;
    }
    
    public static Coordinate getCentroid3D(Triangle t) {
        double xSum = t.p0.x + t.p1.x + t.p2.x;
        double ySum = t.p0.y + t.p1.y + t.p2.y;
        double zSum = t.p0.z + t.p1.z + t.p2.z;
        
        return new Coordinate(xSum / 3, ySum / 3, zSum / 3);
    }
    
    public static Coordinate getRandomPoint(Polygon p) {
        return getRandomPoint(getPolygontoTriangle(p));
    }
    
    public static Coordinate getRandomPoint(Triangle t) {
        Random rand = new Random();
        
        double r1 = rand.nextDouble();
        double r2 = rand.nextDouble();
        
        if(r1 + r2 > 1) {
            r1 = 1 - r1;
            r2 = 1 - r2;
        }
        
        /*if((r1 == r2) && r1 == 1) {
            r2 = rand.nextDouble();
        }*/
        
        double x = (1 - Math.sqrt(r1)) * t.p0.x + (Math.sqrt(r1) * (1 - r2)) * t.p1.x + (Math.sqrt(r1) * (r2)) * t.p2.x;
        double y = (1 - Math.sqrt(r1)) * t.p0.y + (Math.sqrt(r1) * (1 - r2)) * t.p1.y + (Math.sqrt(r1) * (r2)) * t.p2.y;
        double z = (1 - Math.sqrt(r1)) * t.p0.z + (Math.sqrt(r1) * (1 - r2)) * t.p1.z + (Math.sqrt(r1) * (r2)) * t.p2.z;
        
        return new Coordinate(x, y, z);
    }
    
    public static Triangle getPolygontoTriangle(Polygon p) {
        Coordinate c1 = p.getCoordinates()[0];
        Coordinate c2 = p.getCoordinates()[1];
        Coordinate c3 = p.getCoordinates()[2];
        return new Triangle(c1, c2, c3);
    }
    
    public static Coordinate getRandomPoint(Coordinate a, Coordinate b) {
        Random rand = new Random();
        
        double r1 = rand.nextDouble();
        
        double x = a.x + r1 * (a.x - b.x);
        double y = a.y + r1 * (a.y - b.y);
        double z = a.z + r1 * (a.z - b.z);
        
        return new Coordinate(x, y, z);
    }
    
    public static Coordinate getRandomPoint(CellSpace c) {
        List<Polygon> triangles = c.getTriangles();
        Map<Polygon, Double> weight = new HashMap<Polygon, Double>();
        for(Polygon t : triangles) {
            weight.put(t, getPolygontoTriangle(t).area3D());
        }
        Polygon random = getWeightedRandom(weight, new Random());
        return getRandomPoint(getPolygontoTriangle(random));
    }
    
    private static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
        E result = null;
        double bestValue = Double.MAX_VALUE;

        for (E element : weights.keySet()) {
            double value = -Math.log(random.nextDouble()) / weights.get(element);

            if (value < bestValue) {
                bestValue = value;
                result = element;
            }
        }

        return result;
    }
    
    public static Polygon getNearestPolygon(List<Polygon> ps, Coordinate c) {
        double minValue = Double.MAX_VALUE;
        Polygon min = null;
        
        for(Polygon p : ps) {
            double dist = GeometryUtil.distance(p.getCentroid().getCoordinate(), c);
            if(dist < minValue) {
                minValue = dist;
                min = p;
            }
        }
        
        return min;
    }
    
    public static boolean pointInTriangle(Triangle t, Coordinate p) {
        Vector3D a = new Vector3D(t.p0);
        Vector3D b = new Vector3D(t.p1);
        Vector3D c = new Vector3D(t.p2);
        
        Vector3D pv = new Vector3D(p);

        if(sameside(pv, a, b, c) && sameside(pv, b, a, c) && sameside(pv, c, a, b)) {
            /*Vector3D vc1 = cross(
                    subtract(a, b),
                    subtract(a, c)
                    );
            if(Math.abs(subtract(a, pv).dot(vc1)) <= 0.02f) {
                return true;
            }*/
            return true;
        }
        return false;
    }
    
    public static boolean pointInTriangle(Polygon p, Coordinate c) {
        return p.contains(getGeometryFactory().createPoint(c));
    }
    
    private static boolean sameside(Vector3D p1, Vector3D p2, Vector3D A, Vector3D B) {
        Vector3D cp1 = cross(
                subtract(B, A),
                subtract(p1, A));
        Vector3D cp2 = cross(
                subtract(B, A),
                subtract(p2, A));
        if (cp1.dot(cp2) >= 0) return true;
        else return false;
    }
    
    private static Vector3D cross(Vector3D a, Vector3D b) {
        Double x = a.getY() * b.getZ() - a.getZ() * b.getY();
        Double y = a.getZ() * b.getX() - a.getX() * b.getZ();
        Double z = a.getX() * b.getY() - a.getY() * b.getX();
        return new Vector3D(x, y, z);
    }
    
    private static Vector3D subtract(Vector3D a, Vector3D b) {
        return Vector3D.create(new Coordinate(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ()));
    }
}
