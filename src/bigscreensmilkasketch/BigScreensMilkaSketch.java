package bigscreensmilkasketch;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import controlP5.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import mpe.client.*;


public class BigScreensMilkaSketch extends PApplet 
{
	//MPE SETTINGS
	public static float scale = 1f;
	public static boolean MPE = true;
	public static boolean local = true;

	//CLIENT ID
	int ID = 1;
	TCPClient client;

	public static int mWidth, mHeight;

	//GLOBALS
	ControlP5 controlP5;
	ControlGroup preferencesGrp;
	Point controlPos = new Point(10, 20);
	controlP5.Label ampCountLabel;
	controlP5.Label occurCountLabel;
	controlP5.Label currLevelLabel;
	controlP5.Label frameRateLabel;

	//HashMap<String,int[]> dims = new HashMap<String,int[]>();
	//int[] fullscreen_dims = {3840,1080};
	//int[] halfsize_dims = {1770,540};
	//int[] tiny_dims = {885,270};

	//static String selected_dims;

	int sliderWidth = 100;
	int sliderHeight = 10;
	int padding = 10 + sliderHeight;
	int frame_count = 0;
	boolean playing = true;
	int fft_max = 10;
	int fft_min = 1;
	int fft_size = 5;				//Threshold of whether a byte has a "big" frequency
	int amp_count = 0; 				//Count of number of points with fft_size about threshold 
	int occurThreshMax = 45;
	int occurThreshMin = 15;
	int occurrence_threshold = 30; 	//Threshold for classifying an occurrence
	int occurrences = 0; 			//Count of each time the amp_count is greater than x

	int MAX_PARTICLES = 60;
	String frameRateStr;
	Vec2D targetPos;
	float targetStrength;

	ArrayList<Note> notes = new ArrayList<Note>();
	ArrayList<Particle> particles = new ArrayList<Particle>();

	Minim minim;
	AudioPlayer song;
	AudioInput input;
	FFT fft;
	float minLevel, maxLevel, currentLevel;


	/* START UP
	 * 
	 * Set dimensions as "tiny", "halfsize" or "fullscreen" for testing 
	 * on single screen vs. larger screens.
	 * ---------------------------------------------------------------- */
	static public void main(String args[]) {
		//selected_dims = args[0];

		if (local) {
			PApplet.main(new String[] { "bigscreensmilkasketch.BigScreensMilkaSketch" });
		} else {
			PApplet.main(new String[] { "--present","--exclusive","bigscreensmilkasketch.BigScreensMilkaSketch" });

		}
	}



	/* SETUP 
	 * --------------------------------------------------------------- */	
	public void setup() 
	{
		//if using MPE
		if(MPE) {
			String path = "mpefiles/";
			if(local) {
				path += "local/mpe" + ID + ".ini";
			} else {
				ID = IDGetter.getID();
				path += "6screens/mpe" + ID + ".ini";
			}
			client = new TCPClient(path, this);

			// the size is determined by the client's local width and height
			size(client.getLWidth(), client.getLHeight());
			mWidth = client.getMWidth();
			mHeight = client.getMHeight();
		} else {
			size(parseInt(11520*scale),parseInt(1080*scale));
			mWidth = 11520;
			mHeight = 1080;
		}

		//set randomSeed
		randomSeed(1);

		if(MPE) {
			client.start();
		}


		//SKETCH SETTINGS

		//		dims.put("fullscreen",fullscreen_dims);
		//		dims.put("halfsize",halfsize_dims);
		//		dims.put("tiny", tiny_dims);
		//size(dims.get(selected_dims)[0], dims.get(selected_dims)[1]);		

		smooth();
		frameRate(30);
		//initGUI();

		minim = new Minim(this);
		song = minim.loadFile("Houbava Milka (Beautiful Milka).mp3");
		input = minim.getLineIn();
		song.play();
		fft = new FFT(song.bufferSize(), song.sampleRate());
		maxLevel = 0;
		minLevel = Float.MAX_VALUE;

		targetPos = new Vec2D(0,0);
		targetStrength = 0;
		MAX_PARTICLES = fft.specSize();
		initParticles(MAX_PARTICLES);
	}

	/* DRAW
	 *  ---------------------------------------------------------------- */
	public void draw() 
	{
		if(MPE && !local) {
			frame.setLocation(0,0);
		} else if (MPE && local) {
			frame.setLocation(ID* client.getLWidth(), 0);
		}

		if(!MPE) {
			frameEvent(null);
		}
	}



	/* FRAMEVENT
	 *  ---------------------------------------------------------------- */
	public void frameEvent(TCPClient cl) 
	{

		//frame.setLocation(0, 0);
		background(100);

		if (!MPE) {
			scale(scale);
		}

		fft.forward(song.mix);
		if (amp_count>occurrence_threshold) {
			occurrences++;
		}

		//Sets the color of the circles as a function of the current number of points above freq threshold
		//and the cumulative number of times 
		int c = color(255,255-occurrences,255-occurrences, amp_count*5);

		//println( "rgb: " + red(c) + "," + green(c) + "," + blue(c) + "," + alpha(c) );
		//println("hsb: " + hue(c) + "," + saturation(c) + "," + brightness(c));


		int interval = height/(fft.specSize()-1);
		int fft_ht = height - 100;
		int fft_x = 0+100;
		amp_count = 0;

		//Grab the current mix level
		currentLevel = song.mix.level() * 1000; //scale up the values since they are really small

		//TODO: Better way to get max/min levels:
		/*  9.26.2011 calulated max/min levels
		 *  currentLvl: 0.17185166	maxLevel: 0.17185166	minLevel: 0.009024385
		 *  currentLvl: 171.85167	maxLevel: 171.85167	minLevel: 0.36036965 //scaled by 1000
		 */

		if(currentLevel > maxLevel )  {
			maxLevel = currentLevel;
			//println("currentLvl: "+currentLevel + "\tmaxLevel: " + maxLevel + "\tminLevel: " + minLevel );
		}

		// 0.009 value is from observation, might not need it not sure if i can just use "0"
		if(currentLevel < minLevel && currentLevel > 0.009)   {
			minLevel = currentLevel;	
			//println("\tcurrentLvl: "+currentLevel + "\tmaxLevel: " + maxLevel + " \tminLevel:" + minLevel );
		}

		//map color to current level
		float colorLevel = map((float)currentLevel, 0, (float)171.85167, 0, 100);
		//println("\t\tcolorLevel: " + colorLevel); //debugging

		for(int i=0; i<MAX_PARTICLES; i++) {
			particles.get(i).resetForce();
		}

		for(int i = 0; i < fft.specSize(); i++) 
		{

			if (notes.size()-1 > i) {
				Note n = notes.get(i);
				n.update(c, i*interval, fft_ht-fft.getBand(i)*10, fft.getBand(i)*4, fft.getBand(i)*4);


			} else {
				Note n = new Note(this, c, i*interval, fft_ht-fft.getBand(i)*10, fft.getBand(i)*4, fft.getBand(i)*4);
				notes.add(n);
			}

			if (fft.getBand(i) > fft_size) {
				amp_count++;
			}

			Note tn = notes.get(i); 
			targetPos.x = tn.x;
			targetPos.y = tn.y;
			targetStrength = tn.w;

			if(tn != null) {
				tn.render();				
			}

			fill(100);			
		}

		for(int j=0; j<MAX_PARTICLES; j++) {
			for(int k=0; k<MAX_PARTICLES; k++) {
				if( j != k )
					particles.get(j).addForFlocking(particles.get(k));
			}

			//particles.get(j).addRepulsionForce((float)mouseX, (float)mouseY, 400f, 0.4f);
			//particles.get(i).addClockwiseForce(tn.x, tn.y, tn.w*100, 0.4f);
			particles.get(j).addAttractionForce((float)targetPos.x, (float)targetPos.y, 100*occurrences +1, 0.4f);
			//particles.get(i).addRepulsionForce((float)mouseX, (float)mouseY, 400f, 0.4f);
		}	

		for (int i = 0; i < MAX_PARTICLES; i++){
			Particle p = particles.get(i); 
			p.addFlockingForce();
			p.addDampingForce();
			p.bounceOffWalls();
			p.update();
			fill(255,0,0);
			p.draw();	
		}

		frame_count++;

		//drawDebugOutput();
	}


	void initGUI() 
	{
		//setup controlPanel
		controlP5 = new ControlP5(this);
		preferencesGrp = controlP5.addGroup("preferences", controlPos.x, controlPos.y);

		controlP5.addSlider( "fftSize", fft_min, fft_max, controlPos.x, controlPos.y, sliderWidth, sliderHeight ).setId(1);
		Slider fftSizeSlider = (Slider)controlP5.controller("fftSize");
		fftSizeSlider.setValue(fft_size);
		fftSizeSlider.setGroup(preferencesGrp);

		controlP5.addSlider( "occurThreshold", occurThreshMin, occurThreshMax, controlPos.x, controlPos.y + padding, sliderWidth, sliderHeight ).setId(2);
		Slider occurThreshSlider = (Slider)controlP5.controller("occurThreshold");
		occurThreshSlider.setValue(occurrence_threshold);
		occurThreshSlider.setGroup(preferencesGrp);

		ampCountLabel = new controlP5.Label(this, "Amp Count: "+amp_count, sliderWidth, sliderHeight);
		ampCountLabel.position.x = controlPos.x;
		ampCountLabel.position.y = height-(padding*3);

		occurCountLabel = new controlP5.Label(this, "Occur Count: "+occurrences, sliderWidth, sliderHeight);
		occurCountLabel.position.x = controlPos.x;
		occurCountLabel.position.y = ampCountLabel.position.y + sliderHeight + 5;

		currLevelLabel = new controlP5.Label(this, "Current Level: "+currentLevel, sliderWidth, sliderHeight);
		currLevelLabel.position.x = controlPos.x;
		currLevelLabel.position.y = occurCountLabel.position.y + sliderHeight + 5;
		currLevelLabel.setHeight(40);

		frameRateStr = Float.toString(frameRate);
		frameRateLabel = new controlP5.Label(this, frameRateStr + " fps", sliderWidth, sliderHeight);
		frameRateLabel.position.x = controlPos.x;
		frameRateLabel.position.y = currLevelLabel.position.y + sliderHeight + 5;
		frameRateLabel.setHeight(40);
	}


	void initParticles(int numParticles)
	{
		for (int i=0; i<numParticles; i++){
			Particle p = new Particle(this, (int)random(3, 15));
			p.setInitialCondition( random(0, mWidth), random(0, mHeight), 0, 0 );
			particles.add(p);
		}
	}


	/* MINIM METHODS
	 *  ---------------------------------------------------------------- */
	public void stop() 
	{
		// the AudioPlayer you got from Minim.loadFile()
		song.close();
		// the AudioInput you got from Minim.getLineIn()
		input.close();
		minim.stop();
		super.stop();
	}


	void drawDebugOutput() 
	{	
		frameRateStr = Float.toString(round(frameRate));

		ampCountLabel.set("Amp Count: "+amp_count); 
		occurCountLabel.set("Occur Count: "+occurrences);
		currLevelLabel.set("CURRENT LVL: "+currentLevel);		
		frameRateLabel.set(frameRateStr + "fps");

		/*
		ampCountLabel.draw(this);
		occurCountLabel.draw(this);
		currLevelLabel.draw(this);
		frameRateLabel.draw(this);
		 */
	}


	/* EVENTS
	 *  ---------------------------------------------------------------- */

	public void controlEvent(ControlEvent e)
	{
		//println("got a control event from controller with id "+e.controller().id()); //debugging 
		switch(e.controller().id()) {
		case(1):
			fft_size = (int)(e.controller().value());
		break;
		case(2):
			occurrence_threshold = (int)(e.controller().value());
		break;
		}
	}


	public void keyPressed() 
	{
		if(key==' ') {

		}
	}


	public void mousePressed()
	{
		targetPos.set(mouseX, mouseY);
	}
}
