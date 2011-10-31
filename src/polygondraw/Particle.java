package polygondraw;

import processing.core.PApplet;
import geomerative.*;
import toxi.geom.*;
import toxi.color.*;

public class Particle extends Flocking {

	PApplet parent;
	RShape mRs;
	TColor mColor;
	
	Particle(PApplet p, Vec3D pos, Vec3D vel) 
	{
		super(p, pos, vel);
		parent = p;
		
	}

	public void draw() 
	{
		
		RMatrix mat = new RMatrix();
		mat.translate(-mRs.getTopLeft().x, -mRs.getTopLeft().y);
		mat.translate(mPos.x, mPos.y);
		
		mRs.transform(mat);
		
		//debugging
		//parent.fill(255,0,0);
		parent.ellipse(mPos.x, mPos.y, 5, 5);
		
		//mRs.draw();

	}

}
