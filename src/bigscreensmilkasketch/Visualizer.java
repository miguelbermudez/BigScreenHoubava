package bigscreensmilkasketch;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import processing.core.*;
import toxi.math.MathUtils;

public class Visualizer extends PApplet {
	
	PApplet parent;
	static Random RandomSeed = MathUtils.RND;
	
	public Visualizer(PApplet p) {
		parent = p;		
		parent.randomSeed(128);
	}
	
	public void vizSetup() {
		
	}
	
	public void vizDraw(){
		
		
	}
	
	public float[] getDataAsArray(String songData) {
		float[] data = parseFloat(StringUtils.split(songData,","));
		return data;
	}

}
