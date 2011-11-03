/**
 * 
 */
package bigscreensmilkasketch;

import particleSystem.Particle;
import particleSystem.ParticleSystem;
import processing.core.*;

/**
 * @author Miguel Bermudez
 *
 */
public class PhylloSpiralViz extends Visualizer {

	float rotation;
	float spacing;
	int num;
	int totalParticles = 0;
	
	private PVector pos;
	private PVector origin;
	private PVector[] spiralCords;
	private ParticleSystem particleSystem;

	//Enivonmental Forces
	private boolean GRAVITY = true;
	private PVector gravity;
	
	public PhylloSpiralViz(PApplet p) {
		super(p);
		rotation = 137.51f;
		spacing = 8;
		num = 1024;
		gravity = new PVector(0f, 0.5f);
	}
	
	
	public void vizSetup() 
	{
		parent.noStroke();
		particleSystem = new ParticleSystem(this);
		initParticles();
		createSprial();
	}
	
	
	public void vizDraw(String songDataString)
	{
		float[] songData = getDataAsArray(songDataString);
		localDraw(songData);
	}
	
	
	public void setOrigin(PVector _origin)
	{
		origin = _origin;
	}
	
	
	private void localDraw(float[] songData) 
	{
		parent.background(0);
		for (int i = 0; i < songData.length; i++) {
			Particle p1 = particleSystem.particles.get(i);
			PVector target = spiralCords[i];
			p1.arrive(target);
			if(GRAVITY) p1.applyForce(gravity);
			p1.update();
			
			parent.fill(255);
			parent.ellipse(p1.getLocation().x, p1.getLocation().y, 4, 4);
		}
	}
	
	
	private void initParticles()
	{
		for(int i=0; i < num; i++) {
		    
		    PVector pv = new PVector(origin.x, origin.y);		    
		    Particle p = new Particle(this, pv);
		    particleSystem.particles.add(p);
		    particleSystem.totalNumOfParticles = totalParticles;
		}
	}
	
	
	private void createSprial()
	{
		spiralCords = new PVector[num];
		for(int i=0; i < num; i++) {
		    Particle p  = particleSystem.particles.get(i);
			//location
		    float radius = spacing * parent.sqrt(i);
		    float theta = i * parent.radians(rotation);
		    
		    float x = radius * parent.cos(theta);
		    float y = radius * parent.sin(theta);
		    
		    PVector pv = new PVector( (p.getLocation().x + x), (p.getLocation().y + y) );
		    spiralCords[i] = pv;
		}
			
	}
	

	
	

}
