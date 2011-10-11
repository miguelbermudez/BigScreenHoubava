package polygondraw;

import processing.core.PApplet;
import geomerative.*;

import java.util.Arrays.*;


public class PolyDraw extends PApplet {
	
	RShape shp, polyshp, newShp, indivShp;
	RPath indivPath;
	
	RPoint[] indivPathPts;
	
	RPoint[][] pointPaths;
	
	int counter = 0;
	
	static public void main(String args[]) {
		PApplet.main(new String[] { "polygondraw.PolyDraw" });
	}
	
	
	/* SETUP 
	 * --------------------------------------------------------------- */
	public void setup() 
	{
		size(600, 800);
		background(255);
		smooth();
		
		// VERY IMPORTANT: Always initialize the library before using it
		RG.init(this);
		shp = RG.loadShape("kalojan_outline.svg");
		shp.centerIn(g); //fit in window
		polyshp = shp.children[2]; //get the crazy one
		
		pointPaths = polyshp.getPointsInPaths();
		//indivPathPts = pointPaths[50];
		//indivPath = new RPath(indivPathPts);
		
		//what we want to test in terms of indiv drawing paths
		//indivShp = new RShape(indivPath);
		indivShp = polyshp.children[counter];
		
		println("crazy children: "+ polyshp.countChildren());
		//println("indivPathPts: " + indivPathPts.length);
		//println("indivShp pts: " + indivShp.getPoints().length);
		//println("indivShp handles: " + indivShp.getHandles().length);
		
	}

	/* DRAW
	 *  ---------------------------------------------------------------- */
	public void draw() 
	{
		//background(255);
		translate(width/2, height/2);
		
		//float pointSeparation = map(constrain(mouseX, 200, width-200), 200, width-200, 5, 300);
		//RG.setPolygonizer( RG.UNIFORMLENGTH );
		//RG.setPolygonizerLength( pointSeparation );
		//newShp = RG.polygonize( shp.children[2] );
		//RG.ignoreStyles(true);  
		
		noStroke(); 
		//newShp.draw();
		
		//indivShp.setFill(color(26,56,64));
		
		indivShp = polyshp.children[counter];
		RStyle style = indivShp.getStyle();
		int[] RGBColor = toRGB(style.fillColor);
		
		println("shape #\t" + counter + " R:" + RGBColor[0] + " G:" + RGBColor[1] + " B:" + RGBColor[2]);
		indivShp.draw();
		
		if(counter < polyshp.countChildren()-1) {
			counter++;
		}
		else {
			noLoop();
		}
	}
	
	
	/* METHODS
	 *  ---------------------------------------------------------------- */
	
	private int[] toRGB(int c) 
	{
		int R = c >> 16 & 0xFF;
	    int G = c >> 8 & 0xFF;
	    int B = c & 0xFF;

	    int[] cArray = { R, G, B };
	    
	    return cArray;
    }
	      	

}
