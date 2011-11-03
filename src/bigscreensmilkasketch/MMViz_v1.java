package bigscreensmilkasketch;

import java.util.ArrayList;
import java.util.Random;

import particleSystem.Particle;
import particleSystem.ParticleSystem;
import processing.core.PApplet;
import processing.core.PFont;
import toxi.math.MathUtils;


public class MMViz_v1 extends Visualizer {
		
	public MMViz_v1(PApplet p) {
		super(p);
	}
	
	processing.core.PVector		mEye, mCenter, mUp;
	
	ParticleSystem particleSystem;
	PFont font;
	String frameRateStr;
	float mZoneRadius;
	boolean mCentralGravity;
	boolean mFlatten;
	int counter = 0;
	int totalParticles = 0;
	
	private int boost = 4;
	private ArrayList<MShape> shapes = new ArrayList<MShape>();
	private int circColor = color(255,50,50);
	
	
	public void vizSetup() {
		parent.noStroke();

		particleSystem = new ParticleSystem(this);
		
		int fft_length = 513;
		
		for (int i = 0; i < fft_length; i++) {

			processing.core.PVector pos = new processing.core.PVector(0, BigScreensMilkaSketch.mHeight/2, 0);
			
			Particle p = new Particle(this, pos);
			
			particleSystem.particles.add(p);
		}
		
		//println(ParticleController.mParticles.size()); //debugging
		totalParticles = particleSystem.particles.size();
		particleSystem.totalNumOfParticles = totalParticles;
		System.out.println("setup");
		
	}
	
	public void vizDraw(String songDataString) {
		float[] songData = getDataAsArray(songDataString);
		localDraw(songData);
	}
	
	public void localDraw(float[] songData) {
		parent.background(255);		
		for(int i = 0; i < songData.length; i++)
		{
				Particle p1 = (Particle) particleSystem.particles.get(i);	
				//p1.setVelocity(new processing.core.PVector(songData[i],songData[i]));
				p1.update(new processing.core.PVector(songData[i],parent.random(-1,1)));
				//p1.update();
				p1.seek(new processing.core.PVector(BigScreensMilkaSketch.mWidth,BigScreensMilkaSketch.mHeight/2,0));
				particleSystem.flock(p1);									
				parent.fill(255,0,0,songData[i]*30);
				parent.ellipse(p1.getLocation().x, p1.getLocation().y, 8, 8);
				System.out.println(p1.getLocation().x+", "+p1.getLocation().y);			
		}	
	}

}
