package polygondraw;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import geomerative.*;
import toxi.geom.*;
import toxi.color.*;

public class Particle extends FlockingObject {

	public RShape mRs;
	public TColor mColor;
	public int mGroup;
	
	private PApplet parent;
	
	public Particle(PApplet p, PVector pos) 
	{
		
		super(p, pos);
		parent = p;
		
		mGroup = 0;
		mColor = (TColor) TColor.MAGENTA;
		 
	}
	
	
	//------------------------------------------------------------
	void draw() 
	{
		
		//draw directions
		//super.draw();
		
		//RMatrix mat = new RMatrix();
		//mat.translate(-mRs.getTopLeft().x, -mRs.getTopLeft().y);
		//mat.translate(this.getLocation().x, this.getLocation().y);
		
		//mRs.transform(mat);

		//mRs.draw();
		
		//debugging
		parent.fill(mColor.toARGB());
		parent.ellipse(this.getLocation().x, this.getLocation().y, 4, 4);
		
		//debugging flocking
		// Draw a triangle rotated in the direction of velocity
		//float theta = velocity.heading2D() + parent.radians(90);
		//parent.fill(175);
		//parent.stroke(0);
		//parent.pushMatrix();
		//    parent.translate(getLocation().x,getLocation().y);
		//    parent.rotate(theta);
		//    parent.beginShape(parent.TRIANGLES);
		//    parent.vertex(0, -r*2);
		//    parent.vertex(-r, r*2);
		//    parent.vertex(r, r*2);
		//    parent.endShape();
		//parent.popMatrix();
	}
	
	
	//------------------------------------------------------------
	// Wraparound
	void borders() 
	{
		if (getLocation().x < -r) getLocation().x = parent.width+r;
	    if (getLocation().y < -r) getLocation().y = parent.height+r;
	    if (getLocation().x > parent.width+r) getLocation().x = -r;
	    if (getLocation().y > parent.height+r) getLocation().y = -r;
    }

	
	//------------------------------------------------------------
	public void flock(ArrayList<Particle> flock) 
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
	public PVector separate(ArrayList<Particle> flock) 
	{
		float desiredseparation = 25.0f;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (Particle other : flock) {
	    	float d = PVector.dist(getLocation(),other.getLocation());
	    	// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	    	if ((d > 0) && (d < desiredseparation)) {
	    		// Calculate vector pointing away from neighbor
	    		PVector diff = PVector.sub(getLocation(),other.getLocation());
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
	public PVector align(ArrayList<Particle> flock) {
		float neighbordist = 50;
		  PVector sum = new PVector(0,0);
		  int count = 0;
		  for (FlockingObject other : flock) {
			  float d = PVector.dist(getLocation(),other.getLocation());
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
	public PVector cohesion(ArrayList<Particle> flock) {
		float neighbordist = 50;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (Particle other : flock) {
	    	float d = PVector.dist(getLocation(),other.getLocation());
	    	if ((d > 0) && (d < neighbordist)) {
	    		sum.add(other.getLocation()); // Add location
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
		PVector desired = PVector.sub(target,getLocation());  // A vector pointing from the location to the target
	    // Normalize desired and scale to maximum speed
	    desired.normalize();
	    desired.mult(maxspeed);
	    // Steering = Desired minus Velocity
	    PVector steer = PVector.sub(desired,velocity);
	    steer.limit(maxforce);  // Limit to maximum steering force
	
	    return steer;
    }

}
