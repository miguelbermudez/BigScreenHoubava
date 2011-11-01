package polygondraw;

import java.util.Random;

import processing.core.*;
import geomerative.*;
import toxi.geom.*;
import toxi.color.*;
import toxi.math.*;
import processing.opengl.*;

//import java.util.Arrays.*;


public class PolyDraw extends PApplet {
	
	static Random RandomSeed = MathUtils.RND;
	
	RShape 		shp, polyshp, newShp, indivShp;
	RPath 		indivPath;
	RPoint[] 	indivPathPts;
	
	PVector		mEye, mCenter, mUp;
	
	ParticleController mParticleController;
	PFont font;
	String frameRateStr;
	float mZoneRadius;
	boolean mCentralGravity;
	boolean mFlatten;
	int counter = 0;
	int totalParticles = 0;
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "polygondraw.PolyDraw" });
	}
	
	
	/* SETUP 
	 * --------------------------------------------------------------- */
	public void setup() 
	{
		size(1200, 800, OPENGL);
		background(255);
		//smooth();
		font = loadFont("Menlo-Regular-24.vlw");
		textFont(font, 12);
		
		mCenter				= new PVector(0,0);
		mCentralGravity 	= false;
		mFlatten 			= false;
		mZoneRadius 		= 30.0f;
		mParticleController = new ParticleController(this);
	
		
		// VERY IMPORTANT: Always initialize the library before using it
		RG.init(this);
		shp = RG.loadShape("kalojan_outline.svg");
		shp.centerIn(g); //fit in window
		shp.translate(width/2, height/2); 	//move shp to center of screen
		polyshp = shp.children[2]; //get the crazy one
		
		//pointPaths = polyshp.getPointsInPaths();
		//indivShp = polyshp.children[counter];		
		//println("crazy children: "+ polyshp.countChildren()); //debugging
		
		for (int i = 0; i < polyshp.countChildren()-1; i++) {
			RShape rs = polyshp.children[i];
			//println("#"+i+" "+rs.getPoints().length); //debugging
			RPoint centroid = rs.getCentroid();
			RStyle style = rs.getStyle();
			TColor c = TColor.newARGB(style.fillColor);
			PVector pos = new PVector(centroid.x, centroid.y, 0);
			
			Particle p = new Particle(this, pos);
			p.mRs = rs;
			p.mColor = c;
			
			mParticleController.mParticles.add(p);
		}
		
		//println(ParticleController.mParticles.size()); //debugging
		totalParticles = mParticleController.mParticles.size();
		mParticleController.totalNumOfParticles = totalParticles;
				
	}

	/* DRAW
	 *  ---------------------------------------------------------------- */
	public void draw() 
	{
		background(255);
		fill(0);
		frameRateStr = Float.toString(round(frameRate));
		text((frameRateStr + "\n" + totalParticles + " PARTICLES"), 25, 25);
		
		noStroke();
	
		//draw Particles
		mParticleController.draw();	
	}
	
	
	/* METHODS
	 *  ---------------------------------------------------------------- */
	static public PVector randomVector()
	{
		Vec3D randomVector = Vec3D.randomVector(PolyDraw.RandomSeed);
		return new PVector(randomVector.x, randomVector.y);

	}
	      	

}
