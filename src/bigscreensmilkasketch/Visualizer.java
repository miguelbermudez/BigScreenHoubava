package bigscreensmilkasketch;

import org.apache.commons.lang3.StringUtils;
import processing.core.*;

public class Visualizer extends PApplet {
	
	PApplet parent;
	
	public Visualizer(PApplet p) {
		parent = p;		
	}
	
	public float[] getDataAsArray(String songData) {
		float[] data = parseFloat(StringUtils.split(songData,","));
		return data;
	}
	

}
