package polygondraw;

import processing.core.*;
//import geomerative.*;
import toxi.geom.*;
import toxi.math.MathUtils;

import java.util.ArrayList;

public class ParticleController 
{
	PApplet parent;
	//static ArrayList<FlockingObject> mParticles = new ArrayList<FlockingObject>();
	public ArrayList<FlockingObject> mParticles = new ArrayList<FlockingObject>();
	public int totalNumOfParticles;
	float zoneRadius;
	
	public ParticleController(PApplet p) 
	{
		parent = p;
	}
	
	//------------------------------------------------------------
	//TODO: implement apply force method. For example to add Perlin
	//noise, wind, tonrado, etc. 
	void applyForceToParticles( float zoneRadiusSqrd ) 
	{
		
		
	}
	
	
	//------------------------------------------------------------
	void pullToCenter ( PVector center )
	{
		
	}
		
	
	//------------------------------------------------------------
	public void draw() 
	{
		//for (int i = 0; i < mParticles.size(); i++) {
		
		/* so far only way to speed up
		 * animation is to skip particles. 
		 * there is a way to check it's neighbors using a zone radius but i'm still
		 * working on that.
		 * BTW, it's slow because it's looping over EVERYTHING, EVERY SINGLE TIME
		 * 
		 */
		for (int i = 0; i < totalNumOfParticles; i+=3) {	
			
			Particle p1 = (Particle) mParticles.get(i);			
			p1.flock(mParticles);
			p1.update();
			p1.borders();									
			p1.draw();
		}
	}
	
	
	//------------------------------------------------------------
	//This doesn't do anything right now
	void addParticles( int amt )
	{
		float randomLow = 50.0f;
		float randomHigh = 250.0f;
		
		for (int i = 0; i < amt; i++) {

			PVector randomPos = PVector.mult(PolyDraw.randomVector(), parent.random(randomLow, randomHigh));
			Particle p = new Particle( parent, randomPos);
			mParticles.add(p);
		}
	}
	
}
