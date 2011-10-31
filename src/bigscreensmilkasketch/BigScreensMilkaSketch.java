package bigscreensmilkasketch;

import processing.core.PApplet;
import java.util.ArrayList;
import mpe.client.*;
import org.apache.commons.lang3.StringUtils;

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

	int frame_count = 0;
	boolean playing = true;

	//These three variables supply the measure of big the sound is in a moment, 
	//and its cumulative "bigness" over time
	int fft_size = 5; //Threshold of whether a byte has a "big" frequency
	int amp_count = 0; //Count of number of points with fft_size about threshold 
	int occurrence_threshold = 30; //Threshold for classifying an occurrence
	int occurrences = 0; //Count of each time the amp_count is greater than x

	int opening_back = color(135,157,250);
	int closing_back = color(0);

	
	ArrayList<Note> notes1 = new ArrayList<Note>();
	
	float[] fft; 
	float minLevel, maxLevel, currentLevel;

	
	static public void main(String args[]) {
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
		
		if(MPE) {
			client.start();
		}

		smooth();
		smooth();
		frameRate(20);
				
		maxLevel = 0;
		minLevel = Float.MAX_VALUE;
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
		if (!MPE) {
			scale(scale);
		}
			
		background(255);
		  
		//Fades the original background blue to black, according to how many time
		float delta_backR = red(closing_back)-red(opening_back);
		float delta_backG = green(closing_back)-green(opening_back);
		float delta_backB = blue(closing_back)-blue(opening_back);
		float it = 200; //This is a somewhat arbitrary number; it effects how quickly the color dissolves
		int new_col = color(red(opening_back)+occurrences*delta_backR/it,
			green(opening_back)+occurrences*delta_backG/it,
		    blue(opening_back)+occurrences*delta_backB/it);
		fill(new_col);
		rect(0,0,mWidth,mHeight);  
		stroke(255);
	
		if (amp_count>occurrence_threshold) {
			occurrences++;
		}
		  
		//Sets the color of the circles as a function of the current number of points above freq threshold
		//and the cumulative number of times 
		int c = color(255,255-occurrences,255-occurrences, amp_count*5);
		amp_count = 0;
		  
		if (cl.messageAvailable()) {
			String song_fft = cl.getDataMessage()[0]; //We might to make this safer later by checking for a longer array
			fft = parseFloat(StringUtils.split(song_fft,","));
			for(int i = 0; i < fft.length; i++)
			{
				if (notes1.size()-1>i){
					Note n = notes1.get(i);
					n.update(c,fft[i]*4,fft[i]*4);
			    } else {
			    	Note n = new Note(this,c,fft[i]*4,fft[i]*4);
			    	notes1.add(n);
			    }
			    if (fft[i]>fft_size){
			    	amp_count++;
			    }
			    notes1.get(i).render();
			}
		}
	  
		frame_count++;
	}
}
