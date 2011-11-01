/**
 * 
 */
package polygondraw;

import processing.core.*;

/**
 * @author Miguel Bermudez
 *
 */
public class FlockingObject 
{
	PApplet parent;
	PVector location;
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
	    location = pos;
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
	    location.add(velocity);
	    // Reset accelertion to 0 each cycle
	    acceleration.mult(0);
    }
	
	
	//------------------------------------------------------------
	void draw() 
	{
		// Draw a triangle rotated in the direction of velocity
	    float theta = velocity.heading2D() + parent.radians(90);
	    parent.fill(175);
	    parent.stroke(0);
	    parent.pushMatrix();
		    parent.translate(location.x,location.y);
		    parent.rotate(theta);
		    parent.beginShape(parent.TRIANGLES);
		    parent.vertex(0, -r*2);
		    parent.vertex(-r, r*2);
		    parent.vertex(r, r*2);
		    parent.endShape();
	    parent.popMatrix();
    }
	
	
	//------------------------------------------------------------
	
	

}
