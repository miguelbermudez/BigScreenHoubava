package bigscreensmilkasketch;

import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import mpe.client.*;

public class BigScreensMilkaSketch extends PApplet 
{
	
	public static HashMap<String,String> configVars = new HashMap<String, String>();
	
	//MPE SETTINGS
	public static float scale = 1f;
	public static boolean MPE = true;
	public static boolean local = true;

	static //CLIENT ID
	int ID;
	TCPClient client;

	public static int mWidth, mHeight;

	int frame_count = 0;
	boolean playing = true;
	//MMViz_v1 viz = new MMViz_v1(this);
	//VizCircles viz = new VizCircles(this);
	PhylloSpiralViz viz = new PhylloSpiralViz(this);

	
	static public void main(String args[]) {
		String configFileName;
		if (args.length>1) {
			configFileName =args[1];
		} else {
			configFileName = "config.txt";			
		}
		
		try {
			getConfig(configFileName);
			ID = Integer.parseInt(configVars.get("screen"));
			MPE = configVars.get("mpe") != null;
			local = configVars.get("local") != null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		viz.setOrigin( new PVector( 40, (mHeight/2) ) );
		viz.vizSetup();	
		
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
			viz.vizDraw(songData);
			//System.out.println("receiving");
		}
	  
		frame_count++;
	}
	
	public static void getConfig(String fileName) throws IOException {
		/* Get static configuration variables from config.txt */
		File configFile = new File("configuration/"+fileName);
		BufferedReader br = new BufferedReader(new FileReader(configFile));
		try {
			String word = null;
			while ((word=br.readLine())!=null){	
				String[] pair = word.split("=");
				configVars.put(pair[0], pair[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("can't find file at configuration/config.txt");
			e.printStackTrace();
		}
		
	}

}
