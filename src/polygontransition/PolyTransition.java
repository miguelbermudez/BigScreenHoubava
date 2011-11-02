/**
 * 
 */
package polygontransition;

import java.util.ArrayList;

import polygondraw.FlockingObject;
import polygondraw.Particle;
import polygondraw.ParticleController;
import processing.core.*;
import toxi.color.TColor;
import geomerative.*;

/**
 * @author miguelb
 *
 */
public class PolyTransition extends PApplet {
	
	RShape 		shp, polyshp, newShp, indivShp;
	RPoint[][] 	handlesInPaths;	
	ArrayList<RPoint[]>groupPoints = new ArrayList<RPoint[]>();	//collection of all closed paths in the drawing
	ArrayList<PVector>coords = new ArrayList<PVector>();		//collection of current target for particles to flock to 
	
	
	ParticleController mParticleController;
	PFont font;
	String frameRateStr;
	
	int totalParticles = 1020; //255 * 4 = 1020
	int totalNumGroups = 0;
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "polygontransition.PolyTransition" });
	}
	
	/* SETUP 
	 * --------------------------------------------------------------- */
	public void setup() 
	{
		size(1280, 720);
		background(255);
		smooth();
		font = loadFont("Menlo-Regular-24.vlw");
		textFont(font, 12);
		
		mParticleController = new ParticleController(this);
	
		
		// VERY IMPORTANT: Always initialize the library before using it
		RG.init(this);
		
		
		PhyllotacticSpiral spiral = new PhyllotacticSpiral(this, new PVector(width/2, height/2));
		PVector[] spiralPts = spiral.values();
		
		int groupCounter = 0;
		
		//initialize all our particles
		for (int i = 0; i < totalParticles; i++) {
			Particle p = new Particle(this, spiralPts[i]);
//			p.mGroup = groupCounter; 
//			if (groupCounter < totalNumGroups-1) groupCounter++;
//			else groupCounter = 0;
			
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
		mParticleController.seekToTarget(new PVector(mouseX, mouseY));
		//mParticleController.draw();
		mParticleController.drawInitalPosition(); //debugging
	}
	
	
	/* METHODS
	 *  ---------------------------------------------------------------- */
	public void keyPressed()
	{
		switch (key) {
		case '1':
			loadView("OpenningFrieze.svg");
			targetGroup(0);
			break;

		default:
			break;
		}
	}
	
	
	void targetGroup( int targetGroup)
	{
		if (shp != null) {
			int counter = 0;
			for (FlockingObject p : mParticleController.mParticles) {			
				if(counter < handlesInPaths[targetGroup].length)
					p.mGroup = targetGroup;
					coords.add(p.getLocation());
					counter++;
			}
		}
			
			
	}
	
	void loadView(String filename)
	{
		shp = RG.loadShape(filename);
		shp.centerIn(g); //fit in window
		shp.translate(width/2, height/2); 	//move shp to center of screen
		
		//for all the groups (closed paths in geometrive world) in the svg grab their anchor points
		handlesInPaths = shp.getHandlesInPaths();
		totalNumGroups = handlesInPaths.length;
		
		//load all groups into group collection
		for (int i = 0; i < handlesInPaths.length; i++) {
			RPoint[] rpa = handlesInPaths[i];
			println("\tAdding groups to group collection: GRP # "+ i + " with "+rpa.length + " points");
		}
	}

}
