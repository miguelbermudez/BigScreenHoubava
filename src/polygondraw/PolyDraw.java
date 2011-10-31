package polygondraw;

import processing.core.*;
import geomerative.*;
import toxi.geom.*;
import toxi.color.*;
import processing.opengl.*;

//import java.util.Arrays.*;


public class PolyDraw extends PApplet {
	
	RShape 		shp, polyshp, newShp, indivShp;
	RPath 		indivPath;
	RPoint[] 	indivPathPts;
	
	Vec3D		mEye, mCenter, mUp;
	
	ParticleController mParticleController;
	PFont font;
	String frameRateStr;
	float mZoneRadius;
	boolean mCentralGravity;
	boolean mFlatten;
	int counter = 0;
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "polygondraw.PolyDraw" });
	}
	
	
	/* SETUP 
	 * --------------------------------------------------------------- */
	public void setup() 
	{
		size(1200, 800,OPENGL);
		background(255);
		smooth();
		font = loadFont("Menlo-Regular-24.vlw");
		textFont(font, 12);
		
		mCenter				= (Vec3D) Vec3D.ZERO;
		mCentralGravity 	= false;
		mFlatten 			= false;
		mZoneRadius 		= 30.0f;
		mParticleController = new ParticleController(this);
	
		
		// VERY IMPORTANT: Always initialize the library before using it
		RG.init(this);
		shp = RG.loadShape("kalojan_outline.svg");
		shp.centerIn(g); //fit in window
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
			Vec3D pos = new Vec3D(centroid.x, centroid.y, 0);
			Vec3D vel = Vec3D.randomVector().scale( 2.0f );
			
			Particle p = new Particle(this, pos, vel);
			p.mRs = rs;
			p.mColor = c;
			
			ParticleController.mParticles.add(p);
		}
		
		//println(ParticleController.mParticles.size()); //debugging
		
	}

	/* DRAW
	 *  ---------------------------------------------------------------- */
	public void draw() 
	{
		background(255);
		fill(0);
		frameRateStr = Float.toString(round(frameRate));
		text(frameRateStr, 25, 25);
		
		translate(width/2, height/2);
				
		noStroke(); 
		
		mParticleController.applyForceToParticles( mZoneRadius * mZoneRadius );
		if (mCentralGravity) mParticleController.pullToCenter(mCenter);
		mParticleController.update(mFlatten);
		
		//draw Particles
		mParticleController.draw();
			
		
	}
	
	
	/* METHODS
	 *  ---------------------------------------------------------------- */
	
	      	

}
