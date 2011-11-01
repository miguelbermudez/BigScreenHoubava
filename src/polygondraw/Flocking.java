/**
 * 
 */
package polygondraw;

import java.util.ArrayList;
import processing.core.PVector;

/**
 * @author Miguel Bermudez
 *
 */
public interface Flocking 
{
	void flock( ArrayList<FlockingObject> flock );
	PVector separate( ArrayList<FlockingObject> flock );
	PVector align( ArrayList<FlockingObject> flock );
	PVector cohesion( ArrayList<FlockingObject> flock );
	PVector seek( PVector target );
		
}
