package bigscreensmilkasketch;

import processing.core.PApplet;
import controlP5.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

import java.awt.Point;
import java.util.ArrayList;


public class BigScreensMilkaSketch extends PApplet 
{

	//GLOBALS
	ControlP5 controlP5;
	ControlGroup preferencesGrp;
	Point controlPos = new Point(10, 20);
	controlP5.Label ampCountLabel;
	controlP5.Label occurCountLabel;
	controlP5.Label currLevelLabel;
	
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
	
	ArrayList<Note> notes = new ArrayList<Note>();
	
	Minim minim;
	AudioPlayer song;
	AudioInput input;
	FFT fft;
	float minLevel, maxLevel, currentLevel;

	
	/* INIT
	 * --------------------------------------------------------------- */
	public void init() 
	{
		/*
		frame.removeNotify();
		frame.setUndecorated(true);
		frame.addNotify();
		*/
		super.init();
	}
	
	
	/* SETUP 
	 * --------------------------------------------------------------- */	
	public void setup() 
	{
		size(1280, 450);
		smooth();
		frameRate(30);
		initGUI();
		
		minim = new Minim(this);
		song = minim.loadFile("Houbava Milka (Beautiful Milka).mp3");
		input = minim.getLineIn();
		song.play();
		fft = new FFT(song.bufferSize(), song.sampleRate());
		maxLevel = 0;
		minLevel = Float.MAX_VALUE;
	}

	/* DRAW
	 *  ---------------------------------------------------------------- */
	public void draw() 
	{
		background(100);
		
		
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
		println("\t\tcolorLevel: " + colorLevel);
		
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
			if(tn != null) {
				tn.render();
			}
			fill(100);
	    }
		
		frame_count++;
		
		
		drawDebugOutput();
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
		ampCountLabel.position.y = height-(padding*2);
		
		occurCountLabel = new controlP5.Label(this, "Occur Count: "+occurrences, sliderWidth, sliderHeight);
		occurCountLabel.position.x = controlPos.x;
		occurCountLabel.position.y = ampCountLabel.position.y + sliderHeight + 5;
		
		currLevelLabel = new controlP5.Label(this, "Current Level: "+currentLevel, sliderWidth, sliderHeight);
		currLevelLabel.position.x = controlPos.x;
		currLevelLabel.position.y = occurCountLabel.position.y + sliderHeight + 5;
		currLevelLabel.setHeight(40);
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
		ampCountLabel.set("Amp Count: "+amp_count); 
		occurCountLabel.set("Occur Count: "+occurrences);
		currLevelLabel.set("CURRENT LVL: "+currentLevel);
		
		ampCountLabel.draw(this);
		occurCountLabel.draw(this);
		
		currLevelLabel.draw(this);
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
}
