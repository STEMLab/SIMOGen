/**
 * 
 */
package edu.pnu.model.movingobject;

import java.util.Collections;
import java.util.List;

import edu.pnu.movement.FixedWayPoint;
import edu.pnu.movement.Movement;

/**
 * @author hgryoo
 *
 */
public class MovingObjectType {
    
    public MovingObjectType getSuperType() {
        return null;
    }
    
    public Class<? extends Movement> getDefaultMovement() {
        return FixedWayPoint.class;
    }
    
    public List<Class<? extends Movement>> getMovements() {
        return Collections.emptyList();
    }
    
    public Class<? extends Movement> getTerminateMovement() {
        return null;
    }
    
    public String getId() {
        return null;
    }
    
    public String getName() {
        return null;
    }
    
    public String getDescription() {
        return null;
    }
    
}
