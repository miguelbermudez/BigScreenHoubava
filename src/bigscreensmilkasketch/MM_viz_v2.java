package bigscreensmilkasketch;

import java.util.ArrayList;
import java.util.Random;

import particleSystem.Particle;
import particleSystem.ParticleSystem;
import vectorloader.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import toxi.math.MathUtils;


public class MM_viz_v2 extends Visualizer {
		
	public MM_viz_v2(PApplet p) {
		super(p);
	}
	
	//static Random RandomSeed = MathUtils.RND;
		
	ParticleSystem particleSystem;
	//String frameRateStr;

	int counter = 0;
	int totalParticles = 0;
	
	private int boost = 4;
	private VectorLoader vector;
	private ArrayList<PVector> vectorPts;
	private ArrayList<PVector> vectorHandles;
	
	private int circColor = color(255,50,50);
	
	
	public void vizSetup() {
		vector = new VectorLoader(parent,"kalojan_outline.svg");
		vectorPts = vector.getPoints();
		vectorHandles = vector.getHandles();
		particleSystem = new ParticleSystem(this);
		for (int i=0; i < vectorPts.size(); i++) {
			Particle p = new Particle(this, new PVector(parent.random(0,BigScreensMilkaSketch.mWidth),parent.random(0,BigScreensMilkaSketch.mHeight)));
			particleSystem.particles.add(p);
		}
		
		parent.noStroke();

		System.out.println("setup");
		
	}
	
	public void vizDraw(String songDataString) {
		float[] songData = getDataAsArray(songDataString);
		localDraw(songData);
	}
	
	public void localDraw(float[] songData) {
		parent.background(255);		
		for (int i=0; i < vectorHandles.size();i++) {
			parent.fill(0);
			parent.ellipse(vectorHandles.get(i).x,vectorHandles.get(i).y,5,5);
		}
		
		for(int i = 0; i < particleSystem.particles.size(); i++)
		{
			Particle p = particleSystem.particles.get(i);
			//particleSystem.flock(p);
			p.arrive(vectorPts.get(i));
			p.update();
			int sizeBoost = 2;
			int alphaBoost = 30;
			float songVal = songData[i%512];
			//parent.fill(255,0,0,songVal*alphaBoost);
			//parent.ellipse(p.getLocation().x, p.getLocation().y, songVal*sizeBoost, songVal*sizeBoost);	
			parent.fill(255,0,0);
			parent.ellipse(p.getLocation().x, p.getLocation().y, 5, 5);
		}	
	}

}
