package bigscreensmilkasketch;

/**
 * Particle Class
 * @author Miguel Bermduez
 * September 27, 2011
 *
 */

import processing.core.*;
import toxi.geom.*;
import toxi.color.*;

class FlockingForce 
{
	int count;
	Vec2D sum;
	float distance;
	float strength;
	
	FlockingForce() {
		sum = new Vec2D();
	}
}


public class Particle 
{
	Vec2D pos;
	Vec2D vel;
	Vec2D frc;
	FlockingForce cohesion;
	FlockingForce seperation;
	FlockingForce alignment;
	PApplet parent;
	float damping;
	float dampingOnBounce;
	int particleSize;
	boolean bConsiderDistance;
	
	Particle(PApplet p, int t_particleSize)
	{
		parent = p;	
		pos = new Vec2D();
		vel = new Vec2D();
		frc = new Vec2D();
		
		particleSize = t_particleSize;
		setInitialCondition(0, 0, 0, 0);
		
		//Flocking
		cohesion 	= new FlockingForce();
		seperation 	= new FlockingForce();
		alignment 	= new FlockingForce();
		
		damping = 0.01f;
		seperation.distance 	= 16;
		alignment.distance 		= 80;
		cohesion.distance 		= 90;
		
		seperation.strength		= 0.13f;
		cohesion.strength		= 0.06f;
		alignment.strength		= 0.017f;
		
		bConsiderDistance = false;
	}
	
	
	//------------------------------------------------------------
	void resetForce() 
	{
		//reset frc every frame
		frc.set(0,0);
		
		//reset flocking
		cohesion.count 	 = 0;
		seperation.count = 0;
		alignment.count  = 0;
		
		cohesion.sum.set(0,0);
		seperation.sum.set(0,0);
		alignment.sum.set(0,0);
	}
	
	//------------------------------------------------------------
	void addForce(float x, float y)
	{
		frc.x = frc.x + x;
		frc.y = frc.y + y;
	}
	
	//------------------------------------------------------------
	void addRepulsionForce(float x, float y, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(x,y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x + diff.x * scale * pct;
			frc.y = frc.y + diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addAttractionForce(float x, float y, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(x,y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x - diff.x * scale * pct;
			frc.y = frc.y - diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addClockwiseForce(float x, float y, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(x,y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x - diff.x * scale * pct;
			frc.y = frc.y + diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addCounterClockwiseForce(float x, float y, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(x,y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x + diff.x * scale * pct;
			frc.y = frc.y - diff.y * scale * pct;
		}
	}
	
	//------------------------------------------------------------
	void addRepulsionForce(Particle p, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(p.pos.x ,p.pos.y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x + diff.x * scale * pct;
			frc.y = frc.y + diff.y * scale * pct;
			p.frc.x = p.frc.x - diff.x * scale * pct;
			p.frc.y = p.frc.y - diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addAttractionForce(Particle p, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(p.pos.x ,p.pos.y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x - diff.x * scale * pct;
			frc.y = frc.y - diff.y * scale * pct;
			p.frc.x = p.frc.x + diff.x * scale * pct;
			p.frc.y = p.frc.y + diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addClockwiseForce(Particle p, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(p.pos.x ,p.pos.y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x - diff.x * scale * pct;
			frc.y = frc.y + diff.y * scale * pct;
			p.frc.x = p.frc.x + diff.x * scale * pct;
			p.frc.y = p.frc.y - diff.y * scale * pct;
		}
	}
	
	//------------------------------------------------------------
	void addCounterClockwiseForce(Particle p, float radius, float scale)
	{
		Vec2D posOfForce = new Vec2D();
		Vec2D diff = new Vec2D();
		
		posOfForce.set(p.pos.x ,p.pos.y);
		diff = pos.sub(posOfForce);
		float length = diff.magnitude();
		
		boolean bAmCloseEnough = true;
		if(radius > 0) {
			if(length > radius) {
				bAmCloseEnough = false;
			}
		}
		
		if(bAmCloseEnough == true) {
			float pct = 1 - (length/radius);
			diff.normalize();
			frc.x = frc.x + diff.x * scale * pct;
			frc.y = frc.y - diff.y * scale * pct;
			p.frc.x = p.frc.x - diff.x * scale * pct;
			p.frc.y = p.frc.y + diff.y * scale * pct;
		}
	}
	
	
	//------------------------------------------------------------
	void addDampingForce() 
	{
		frc.x = frc.x - vel.x * damping;
		frc.y = frc.y - vel.y * damping;
	}
	
	
	//------------------------------------------------------------
	void setInitialCondition(float px, float py, float vx, float vy) 
	{
		pos.set(px, py);
		vel.set(vx, vy);
	}
	

	//------------------------------------------------------------
	void update() 
	{
		vel = vel.add(frc);
		pos = pos.add(vel);
	}
	
	
	//------------------------------------------------------------
	void draw() 
	{
		parent.ellipse(pos.x, pos.y, particleSize, particleSize);
	}
	
	
	//------------------------------------------------------------
	void bounceOffWalls() 
	{
		boolean bDampedOnCollision = true;
		boolean bDidICollide = false;
		
		//walls
		float minx = 0;
		float miny = 0;
		float maxx = parent.width;
		float maxy = parent.height;
		
		if(pos.x > maxx) {
			pos.x = maxx;
			vel.x *= -1;
			bDidICollide = true;	
		} else if (pos.x < minx) {
			pos.x = minx;
			vel.x *= -1;
			bDidICollide = true;
		}
		
		if (pos.y > maxy){
			pos.y = maxy;
			vel.y *= -1;
			bDidICollide = true;
		} else if (pos.y < miny){
			pos.y = miny; 
			vel.y *= -1;
			bDidICollide = true;
		}
		
		if (bDidICollide == true && bDampedOnCollision == true){
			vel.scale(dampingOnBounce);
		}	
	}
	
	
	//------------------------------------------------------------
	void addForFlocking(Particle p)
	{
		Vec2D diff, diffNormalized;
		float distance;
		
		diff = new Vec2D();
		diffNormalized = new Vec2D();
		
		diff = p.pos.sub(pos);
		distance = diff.magnitude();
		diffNormalized = diff.normalize();
		
		if (distance > 0 && distance < seperation.distance) {
			seperation.sum = seperation.sum.add(diffNormalized);
			seperation.count++;
		}
		
		if(distance > 0 && distance < alignment.distance) {
			alignment.sum = alignment.sum.add(p.vel.getNormalized());
			alignment.count++;
		}
		
		if(distance > 0 && distance < cohesion.distance) {
			cohesion.sum = cohesion.sum.add(p.pos);
			cohesion.count++;
		}
	}
	

	//------------------------------------------------------------
	void addFlockingForce() 
	{
		//seperation
		if(seperation.count > 0) {
			//seperation.sum = seperation.sum.scale((1/seperation.count));	//this is weird, you can't divide a vector like in C
			seperation.sum = new Vec2D( (seperation.sum.x  / seperation.count), (seperation.sum.y / seperation.count) );
			float sepFrc = seperation.strength;
			frc.sub(seperation.sum.normalize().scale(sepFrc));
		}
		
		//alignment
		if(alignment.count > 0) {
			//alignment.sum = alignment.sum.scale((1/alignment.count)); 		//* same as above
			alignment.sum = new Vec2D( (alignment.sum.x  / alignment.count), (alignment.sum.y / alignment.count) );
			float alignFrc = alignment.strength;
			frc.add(alignment.sum.scale(alignFrc));
		}
		
		//cohesion
		if(cohesion.count > 0) {
			//cohesion.sum = cohesion.sum.scale((1/cohesion.count)); 			//* same as above
			cohesion.sum = new Vec2D( (cohesion.sum.x  / cohesion.count), (cohesion.sum.y / cohesion.count) );
			cohesion.sum.sub(pos);
			float cohFrc = cohesion.strength;
			frc.add(cohesion.sum.normalize().scale(cohFrc));
		}
	}
}
