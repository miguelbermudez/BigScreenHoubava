package polygondraw;

import processing.core.*;
//import geomerative.*;
import toxi.geom.*;

import java.util.ArrayList;

public class ParticleController 
{
	PApplet parent;
	static ArrayList<Particle> mParticles = new ArrayList<Particle>();
	
	ParticleController(PApplet p) 
	{
		parent = p;
	}
	
	//------------------------------------------------------------
	void applyForceToParticles( float zoneRadiusSqrd ) 
	{
		
		for (int i = 0; i < mParticles.size(); i++) {
			Particle p1 = mParticles.get(i);
			
			int j = i;
			for( j++; j != mParticles.size(); j++) {
				Particle p2 = mParticles.get(j);
				Vec3D dir = p1.mPos.sub(p2.mPos);
				float distSqrd = dir.magSquared();
				
				if (distSqrd <= zoneRadiusSqrd ) { // SEPARATION
					float F = ( zoneRadiusSqrd / distSqrd - 1.0f) * 0.01f;
					dir.normalize();
					dir.scaleSelf(F);
					
					p1.mAcc.addSelf(dir);
					p2.mAcc.subSelf(dir);
				}
			}			
		}
	}
	
	
	//------------------------------------------------------------
	void pullToCenter ( Vec3D center )
	{
		for (int i = 0; i < mParticles.size(); i++) {
			Particle p = mParticles.get(i);
			p.pullToCenter(center);
		}
	}
	
	
	//------------------------------------------------------------
	void update ( boolean flatten ) 
	{
		for (int i = 0; i < mParticles.size(); i++) {
			Particle p = mParticles.get(i);
			p.update(flatten);
		}
	}
	
	
	//------------------------------------------------------------
	void draw() 
	{
		for (int i = 0; i < mParticles.size(); i++) {
			Particle p = mParticles.get(i);
			p.draw();
		}
			
	
	}
	
	
	//------------------------------------------------------------
	void addParticles( int amt )
	{
		for (int i = 0; i < amt; i++) {
			Vec3D pos =  Vec3D.randomVector().scale( parent.random(50.0f, 250.0f) );
			Vec3D vel = Vec3D.randomVector().scale( 2.0f );
			Particle p = new Particle( parent, pos, vel );
			mParticles.add(p);
		}
	}
}
