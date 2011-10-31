package polygondraw;

import java.util.ArrayList;
import processing.core.*;

public class Boid {
	PApplet parent;
	PVector location;
	PVector velocity;
	PVector acceleration;
	float 	r;
	float 	maxforce;
	float 	maxspeed;
	
	Boid(PApplet p, float x, float y) {
	    parent = p;
		acceleration = new PVector(0,0);
	    velocity = new PVector(parent.random(-1,1),parent.random(-1,1));
	    location = new PVector(x,y);
	    r = 2.0f;
	    maxspeed = 3f;
	    maxforce = 0.05f;
    }
	
	void run(ArrayList boids) 
	{
		flock(boids);
		update();
		borders();
		render();
	}
	
	void applyForce(PVector force) 
	{
		// We could add mass here if we want A = F / M
	    acceleration.add(force);
	  }
	
	// We accumulate a new acceleration each time based on three rules
	void flock(ArrayList<Boid> boids) 
	{
	    PVector sep = separate(boids);   // Separation
	    PVector ali = align(boids);      // Alignment
	    PVector coh = cohesion(boids);   // Cohesion
	    // Arbitrarily weight these forces
	    sep.mult(1.5f);
	    ali.mult(1.0f);
	    coh.mult(1.0f);
	    // Add the force vectors to acceleration
	    applyForce(sep);
	    applyForce(ali);
	    applyForce(coh);
	  }
	
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
	
	
	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	PVector seek(PVector target) 
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
	
	void render() 
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
	
	
	// Wraparound
	void borders() 
	{
		if (location.x < -r) location.x = parent.width+r;
	    if (location.y < -r) location.y = parent.height+r;
	    if (location.x > parent.width+r) location.x = -r;
	    if (location.y > parent.height+r) location.y = -r;
    }
	
	
	// Separation
	// Method checks for nearby boids and steers away
	PVector separate (ArrayList<Boid> boids) 
	{
		float desiredseparation = 25.0f;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (Boid other : boids) {
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

	
	  // Alignment
	  // For every nearby boid in the system, calculate the average velocity
	  PVector align (ArrayList<Boid> boids) 
	  {
		  float neighbordist = 50;
		  PVector sum = new PVector(0,0);
		  int count = 0;
		  for (Boid other : boids) {
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
	  
	  
	// Cohesion
	// For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
	PVector cohesion (ArrayList<Boid> boids) 
	{
		float neighbordist = 50;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (Boid other : boids) {
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
	
	
	
	
}
