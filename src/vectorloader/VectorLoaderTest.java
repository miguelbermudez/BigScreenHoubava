package vectorloader;

import processing.core.PApplet;

public class VectorLoaderTest extends PApplet {
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "vectorloader.VectorLoaderTest" });
	}
	
	/* SETUP 
	 * --------------------------------------------------------------- */
	public void setup() 
	{
		size(600, 600, OPENGL);
		background(255);
		//smooth();
		
		VectorLoader vectorloader = new VectorLoader(this, "kalojan_outline.svg");
		
		
	}
	
	public void draw()
	{
		
	}

}
