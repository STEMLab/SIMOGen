/**
 * 
 */
package edu.pnu.stemlab.model.geometry.impl;

import edu.pnu.stemlab.model.geometry.Envelope;
import edu.pnu.stemlab.model.geometry.Geometry;
import edu.pnu.stemlab.model.geometry.Point;

/**
 * @author hgryoo
 *
 */
public abstract class JTSGeometryImpl implements Geometry {
    
        private com.vividsolutions.jts.geom.Geometry jtsGeom;
        
        public JTSGeometryImpl(com.vividsolutions.jts.geom.Geometry geom) {
            this.jtsGeom = geom;
        }
        
        protected com.vividsolutions.jts.geom.Geometry getJTSGeometry() {
            return jtsGeom;
        }
        
	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#isSimple()
	 */
	public boolean isSimple() {
	    return getJTSGeometry().isSimple();
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#isCycle()
	 */
	public abstract boolean isCycle();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#distance(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public abstract boolean distance(Geometry geometry);

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getEnvelope()
	 */
	public abstract Envelope getEnvelope();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getCentroid()
	 */
	public abstract Point getCentroid();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getRepresentativePoint()
	 */
	public abstract Point getRepresentativePoint();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getBoundary()
	 */
	public abstract Geometry getBoundary();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getConvexHull()
	 */
	public abstract Geometry getConvexHull();

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#getBuffer(double)
	 */
	public abstract Geometry getBuffer(double distance);

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#clone()
	 */
	public abstract Geometry clone() throws CloneNotSupportedException;

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#disjoint(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean disjoint(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#touches(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean touches(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#intersects(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean intersects(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#crosses(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean crosses(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#within(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean within(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#contains(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean contains(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#overlaps(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean overlaps(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#covers(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean covers(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#coveredBy(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean coveredBy(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.pnu.stemlab.model.geometry.Geometry#equals(edu.pnu.stemlab.model.geometry.Geometry)
	 */
	public boolean equals(Geometry g) {
		// TODO Auto-generated method stub
		return false;
	}

}
