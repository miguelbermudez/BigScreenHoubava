/**
 * 
 */
package polygondraw;

import processing.core.*;

/**
 * @author Miguel Bermudez
 *
 */
public abstract class FlockingObject 
{
	PApplet parent;
	private PVector location;
	PVector velocity;
	PVector acceleration;
	float 	r;
	float 	maxforce;
	float 	maxspeed;
	
	FlockingObject(PApplet p, PVector pos)
	{
		parent = p;
		acceleration = new PVector(0,0);
	    velocity = new PVector(parent.random(-1,1),parent.random(-1,1));
	    setLocation(pos);
	    r = 2.0f;
	    maxspeed = 3f;
	    maxforce = 0.05f;
	}
	

	//------------------------------------------------------------
	void applyForce(PVector force) 
	{
		// We could add mass here if we want A = F / M
	    acceleration.add(force);
    }
	
	
	//------------------------------------------------------------
	// Method to update location
	void update() 
	{
		// Update velocity
	    velocity.add(acceleration);
	    // Limit speed
	    velocity.limit(maxspeed);
	    getLocation().add(velocity);
	    // Reset accelertion to 0 each cycle
	    acceleration.mult(0);
    }
	
	
	//------------------------------------------------------------
	abstract void draw();

	
	
	//------------------------------------------------------------
	public void setLocation(PVector location) {
		this.location = location;
	}


	public PVector getLocation() {
		return location;
	}

}
