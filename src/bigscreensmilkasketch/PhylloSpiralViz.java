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
	
	public PhylloSpiralViz(PApplet p, PVector _origin) {
		super(p);
		rotation = 137.51f;
		spacing = 8;
		num = 1024;
		origin = _origin;
	}
	
	
	public void vizSetup() 
	{
		noStroke();
		particleSystem = new ParticleSystem(this);
		initParticles();
		createSprial();
	}
	
	
	public void vizDraw(String songDataString)
	{
		float[] songData = getDataAsArray(songDataString);
		localDraw(songData);
	}
	
	private void localDraw(float[] songData) 
	{
		background(0);
		for (int i = 0; i < songData.length; i++) {
			Particle p1 = particleSystem.particles.get(i);
			PVector target = spiralCords[i];
			
			
			
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
		    float radius = spacing * sqrt(i);
		    float theta = i * radians(rotation);
		    
		    float x = radius * cos(theta);
		    float y = radius * sin(theta);
		    
		    PVector pv = new PVector( (p.getLocation().x + x), (p.getLocation().y + y) );
		    spiralCords[i] = pv;
		}
		
		
	}
	
	
	
	

	
	
	
	

}
