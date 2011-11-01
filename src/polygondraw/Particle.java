package polygondraw;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import geomerative.*;
import toxi.geom.*;
import toxi.color.*;

public class Particle extends FlockingObject implements Flocking {

	PApplet parent;
	RShape mRs;
	TColor mColor;
	
	Particle(PApplet p, PVector pos) 
	{
		
		super(p, pos);
		parent = p; 
	}
	
	
	//------------------------------------------------------------
	void draw() 
	{
		
		//draw directions
		//super.draw();
		
		RMatrix mat = new RMatrix();
		mat.translate(-mRs.getTopLeft().x, -mRs.getTopLeft().y);
		mat.translate(this.location.x, this.location.y);
		
		mRs.transform(mat);
		
		//debugging
		//parent.fill(255,0,0);
		parent.fill(mColor.toARGB());
		parent.ellipse(this.location.x, this.location.y, 8, 8);
		

		//mRs.draw();

	}
	
	
	//------------------------------------------------------------
	// Wraparound
	void borders() 
	{
		if (location.x < -r) location.x = parent.width+r;
	    if (location.y < -r) location.y = parent.height+r;
	    if (location.x > parent.width+r) location.x = -r;
	    if (location.y > parent.height+r) location.y = -r;
    }

	
	//------------------------------------------------------------
	public void flock(ArrayList<FlockingObject> flock) 
	{
		PVector sep = separate(flock);
		PVector ali = align(flock);
		PVector coh = cohesion(flock);

		//Arbitrarily weigh these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
	    coh.mult(1.0f);
	    // Add the force vectors to acceleration
	    applyForce(sep);
	    applyForce(ali);
	    applyForce(coh);
	}
	
	
	//------------------------------------------------------------
	public PVector separate(ArrayList<FlockingObject> flock) 
	{
		float desiredseparation = 25.0f;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (FlockingObject other : flock) {
	    	float d = PVector.dist(location,other.location);
	    	// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	    	if ((d > 0) && (d < desiredseparation)) {
	    		// Calculate vector pointing away from neighbor
	    		PVector diff = PVector.sub(location,other.location);
	    		diff.normalize();
	    		diff.div(d);        // Weight by distance
	    		steer.add(diff);
	    		count++;            // Keep track of how many
	    	}
	    }
	    // Average -- divide by how many
	    if (count > 0) {
	      steer.div((float)count);
	    }

	    // As long as the vector is greater than 0
	    if (steer.mag() > 0) {
	      // Implement Reynolds: Steering = Desired - Velocity
	      steer.normalize();
	      steer.mult(maxspeed);
	      steer.sub(velocity);
	      steer.limit(maxforce);
	    }
	    return steer;
	}
	
	
	//------------------------------------------------------------
	public PVector align(ArrayList<FlockingObject> flock) {
		float neighbordist = 50;
		  PVector sum = new PVector(0,0);
		  int count = 0;
		  for (FlockingObject other : flock) {
			  float d = PVector.dist(location,other.location);
			  if ((d > 0) && (d < neighbordist)) {
				  sum.add(other.velocity);
				  count++;
			  }
		  }
		  if (count > 0) {
			  sum.div((float)count);
			  sum.normalize();
			  sum.mult(maxspeed);
			  PVector steer = PVector.sub(sum,velocity);
			  steer.limit(maxforce);
			  
			  return steer;
			  
		  } else {
			  return new PVector(0,0);
		  }
	}

	//------------------------------------------------------------
	public PVector cohesion(ArrayList<FlockingObject> flock) {
		float neighbordist = 50;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (FlockingObject other : flock) {
	    	float d = PVector.dist(location,other.location);
	    	if ((d > 0) && (d < neighbordist)) {
	    		sum.add(other.location); // Add location
	    		count++;
	    	}
	    }
	    
	    if (count > 0) {
	    	sum.div(count);
	    	return seek(sum);  // Steer towards the location
	    } else {
	      return new PVector(0,0);
	    }
	}
	
	
	//------------------------------------------------------------
	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	public PVector seek(PVector target) 
	{
		PVector desired = PVector.sub(target,location);  // A vector pointing from the location to the target
	    // Normalize desired and scale to maximum speed
	    desired.normalize();
	    desired.mult(maxspeed);
	    // Steering = Desired minus Velocity
	    PVector steer = PVector.sub(desired,velocity);
	    steer.limit(maxforce);  // Limit to maximum steering force
	
	    return steer;
    }


}
