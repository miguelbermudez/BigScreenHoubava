package particleSystem;

import java.util.ArrayList;

import processing.core.*;

public class ParticleSystem {
	
	private PApplet parent;	
	
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public int totalNumOfParticles;
	
	public ParticleSystem(PApplet p) 
	{
		parent = p;
	}


	//------------------------------------------------------------
	public void flock(Particle particle) 
	{
		PVector sep = separate(particle);
		PVector ali = align(particle);
		PVector coh = cohesion(particle);

		//Arbitrarily weigh these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
	    coh.mult(1.0f);
	    // Add the force vectors to acceleration
	    particle.applyForce(sep);
	    particle.applyForce(ali);
	    particle.applyForce(coh);
	}
	
	
	//------------------------------------------------------------
	public PVector separate(Particle particle) 
	{
		float desiredseparation = 25.0f;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (Particle other : particles) {
	    	float d = PVector.dist(particle.getLocation(),other.getLocation());
	    	// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	    	if ((d > 0) && (d < desiredseparation)) {
	    		// Calculate vector pointing away from neighbor
	    		PVector diff = PVector.sub(particle.getLocation(),other.getLocation());
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
	      steer.mult(particle.getMaxSpeed());
	      steer.sub(particle.getVelocity());
	      steer.limit(particle.getMaxForce());
	    }
	    return steer;
	}
	
	
	//------------------------------------------------------------
	public PVector align(Particle particle) {
		float neighbordist = 50;
		  PVector sum = new PVector(0,0);
		  int count = 0;
		  for (Particle other : particles) {
			  float d = PVector.dist(particle.getLocation(),other.getLocation());
			  if ((d > 0) && (d < neighbordist)) {
				  sum.add(other.getVelocity());
				  count++;
			  }
		  }
		  if (count > 0) {
			  sum.div((float)count);
			  sum.normalize();
			  sum.mult(particle.getMaxSpeed());
			  PVector steer = PVector.sub(sum,particle.getVelocity());
			  steer.limit(particle.getMaxForce());
			  
			  return steer;
			  
		  } else {
			  return new PVector(0,0);
		  }
	}

	//------------------------------------------------------------
	public PVector cohesion(Particle particle) {
		float neighbordist = 50;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (Particle other : particles) {
	    	float d = PVector.dist(particle.getLocation(),other.getLocation());
	    	if ((d > 0) && (d < neighbordist)) {
	    		sum.add(other.getLocation()); // Add location
	    		count++;
	    	}
	    }
	    /*
	    if (count > 0) {
	    	sum.div(count);
	    	return particle.seek(sum);  // Steer towards the location
	    } else {
	      return new PVector(0,0);
	    }
	    */
	    return new PVector(0,0);
	}
	

}
