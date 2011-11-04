package particleSystem;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle  {

	PApplet parent;
	private PVector location;
	private PVector velocity;
	private PVector acceleration;
	private float 	r;
	private float 	maxforce;
	private float 	maxspeed;
	
	public Particle(PApplet p, PVector pos) 
	{
		parent = p;
		acceleration = new PVector(0,0);
	    velocity = new PVector(parent.random(0,1),parent.random(0,1));
	    setLocation(pos);
	    r = 3.0f;
	    maxspeed = 5f;
	    maxforce = 0.1f;
	}
	
	//------------------------------------------------------------
	public void applyForce(PVector force) 
	{
		// We could add mass here if we want A = F / M
	    acceleration.add(force);
    }
	
	
	//------------------------------------------------------------
	// Method to update location
	public void update() 
	{
	    velocity.add(acceleration);
	    velocity.limit(maxspeed);
	    getLocation().add(velocity);
	    // Reset accelertion to 0 each cycle
	    acceleration.mult(0);
    }
	
	//------------------------------------------------------------
	// Method that updates location with a new acceleration
	public void update(PVector a) 
	{
	    velocity.add(a);
	    velocity.limit(maxspeed);
	    getLocation().add(velocity);
	    // Reset accelertion to 0 each cycle
	    acceleration.mult(0);
    }
	
	
	//------------------------------------------------------------
	public void seek(PVector target) {
		acceleration.add(steer(target));
	  }
	
	//------------------------------------------------------------
	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	public PVector steer(PVector target) 
	{
		PVector desired = PVector.sub(target, getLocation());  // A vector pointing from the location to the target
	    // Normalize desired and scale to maximum speed
	    desired.normalize();
	    desired.mult(maxspeed);
	    // Steering = Desired minus Velocity
	    PVector steer = PVector.sub(desired,velocity);
	    steer.limit(maxforce);  // Limit to maximum steering force
	
	    return steer;
    }
	
	
	//------------------------------------------------------------
	public void arrive(PVector target)
	{
		PVector desired = PVector.sub(target, getLocation());
		float d = desired.mag();
		
		desired.normalize();
		if(d < 100) desired.mult(maxspeed*(d/100));
		else desired.mult(maxspeed);
		
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(maxforce);
		applyForce(steer);
	}
	
	
	//GETTERS AND SETTERS
	//------------------------------------------------------------
	public void setLocation(PVector location) {
		this.location = location;
	}

	public PVector getLocation() {
		return location;
	}
	
	public void setVelocity(PVector velocity) {
		this.velocity = velocity;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public float getMaxSpeed() {
		return maxspeed;
	}
	
	public float getMaxForce() {
		return maxforce;
	}
	
	//------------------------------------------------------------
	public float direction()
	{
		float theta = velocity.heading2D() + PApplet.radians(90);
		return theta;
		
	}

}
