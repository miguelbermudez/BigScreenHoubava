package audioPlayer;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import bigscreensmilkasketch.BigScreensMilkaSketch;
import mpe.client.*;
import processing.core.*;

public class audioPlayer extends BigScreensMilkaSketch {

	AsyncClient client;
	PFont font;

	boolean message = false;
	
	Minim minim;
	AudioPlayer song;
	AudioInput input;
	FFT fft;
	
	ArrayList<String> songData = new ArrayList<String>();

	//--------------------------------------
	public void setup() {
		// set up the client
		// For testing locally
		if (local) {
			client = new AsyncClient("localhost",9003);
		} else {
			// At NYU
			client = new AsyncClient("128.122.151.64",9003);
			// At IAC
			// client = new AsyncClient("192.168.130.241",9003);
		}
		
		
		size(255, 255);
		smooth();
		frameRate(20);
		font = createFont("Arial", 18);
		
		minim = new Minim(this);
		song = minim.loadFile("Houbava Milka (Beautiful Milka).mp3");
		input = minim.getLineIn();
		song.play();
		fft = new FFT(song.bufferSize(), song.sampleRate());
	}

	//--------------------------------------
	public void draw() {
		fft.forward(song.mix);
		for(int i = 0; i < fft.specSize(); i++)
		  {
			float f = fft.getBand(i)*10000/10000; //this should get rid of too many decimal places
		    songData.add(Float.toString(f));
		    System.out.println(songData.get(i));
		  }
		String msg = StringUtils.join(songData,",");
		client.broadcast(msg);
		songData.clear();
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

	//--------------------------------------
	static public void main(String args[]) {
		PApplet.main(new String[] { "audioPlayer.audioPlayer" });
	}

}
