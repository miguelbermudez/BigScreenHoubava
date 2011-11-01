package bigscreensmilkasketch;

import processing.core.*;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mpe.client.*;

public class BigScreensMilkaSketch extends PApplet 
{
	//MPE SETTINGS
	public static float scale = 1f;
	public static boolean MPE = true;
	public static boolean local = true;

	//CLIENT ID
	int ID = 0;
	TCPClient client;

	public static int mWidth, mHeight;

	int frame_count = 0;
	boolean playing = true;
	VizCircles viz = new VizCircles(this);
	

	
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
			client.DEBUG=false;
			client.start();
		}

		smooth();
		frameRate(20);
				
		
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
		
	public void frameEvent(TCPClient cl) 
	{
		if (!MPE) {
			scale(scale);
		}
		
		if (cl.messageAvailable()) {
			String songData = cl.getDataMessage()[0];
			viz.visualizeData(songData);
		}
		ellipse(20,20,20,20);
	  
		frame_count++;
	}
	


}
