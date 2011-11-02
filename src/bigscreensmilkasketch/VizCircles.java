package bigscreensmilkasketch;

import java.util.ArrayList;
import processing.core.*;

public class VizCircles extends Visualizer {
		
	public VizCircles(PApplet p) {
		super(p);
	}
	
	private int boost = 4;
	private ArrayList<MShape> shapes = new ArrayList<MShape>();
	private int circColor = color(255,50,50);
	
	public void vizSetup(){
		
		
	}
	
	public void vizDraw(String songDataString) {
		float[] songData = getDataAsArray(songDataString);
		vDraw(songData);
	}
	
	
	public void vDraw(float[] songData) {
		parent.background(0,0,100);		
		for(int i = 0; i < songData.length; i++)
		{
			if (shapes.size()-1>i){
				MShape n = shapes.get(i);
				n.update(circColor,songData[i]*boost,songData[i]*boost);
		    } else {
		    	MShape n = new MShape(parent,circColor,songData[i]*boost,songData[i]*boost);
		    	shapes.add(n);
		    }
			shapes.get(i).render();
		}		
	}
	
	public void streamsDraw(float[] songData) {
		
		parent.background(255);	
		parent.stroke(0);
		parent.strokeWeight(4);
		parent.beginShape();
		for(int i = 0; i < songData.length; i++)
		{
			if (i%2==0){
				songData[i]=songData[i]*-1;
			}
			int x = (int) parent.map(songData[i],-255,255,0,BigScreensMilkaSketch.mWidth);
			int y = (int) parent.map(songData[i],-255,255,0,BigScreensMilkaSketch.mWidth);
			parent.vertex(x,y);
			
		}
		parent.endShape();
		
	}
	

}
