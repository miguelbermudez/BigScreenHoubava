package polygondraw;

import processing.core.*;
//import geomerative.*;
import toxi.geom.*;
import toxi.math.MathUtils;

import geomerative.RPoint;

import java.util.ArrayList;

public class ParticleController 
{
	private PApplet parent;
	
	
	public ArrayList mParticles = new ArrayList();
	public int totalNumOfParticles;
	
	public ParticleController(PApplet p) 
	{
		parent = p;
	}
	
	public void seekToTarget( PVector target )
	{
		for (int i = 0; i < totalNumOfParticles; i++) {
			Particle p = (Particle) mParticles.get(i);
			p.applyForce(p.seek(target));
		}
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
	public void drawInitalPosition()
	{
		for (int i = 0; i < totalNumOfParticles; i++) {	
			
			Particle p1 = (Particle) mParticles.get(i);
			p1.draw();
		}
	}
	
	
}
