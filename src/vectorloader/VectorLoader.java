/**
 * 
 */
package vectorloader;

import java.util.ArrayList;

import processing.core.*;
import geomerative.*;

/**
 * @author Miguel Bermudez
 *
 */
public class VectorLoader {
	
	private PApplet parent;
	private RShape shp;
	private ArrayList<PVector> allPoints;
	private ArrayList<PVector> allHandlePoints;
	
	public VectorLoader(PApplet p, String filename)
	{
		parent = p;
		RG.init(p);
		shp = RG.loadShape(filename);
		
		processDrawing();
	}
	
	private void processDrawing()
	{
		RPoint[][] allRPointsInPaths = shp.getPointsInPaths();
		RPoint[][] allHandlesPtsInPaths = shp.getHandlesInPaths();
		
		RPoint[] all_RPoints = allRPointsInPaths[0];	//grab the first group
		RPoint[] all_RHandles = allHandlesPtsInPaths[0];
		
		
		allPoints = new ArrayList<PVector>();
		allHandlePoints = new ArrayList<PVector>();
		
		//populate allPoints PVector list
		for(RPoint rp : all_RPoints) {
			PVector pos = new PVector(rp.x, rp.y);
			allPoints.add(pos);
			//parent.println("allPoints: " + allPoints.size());  //sanity check
		}
		
		//populate allHandlePoints PVector list
		for(RPoint rp : all_RHandles) {
			PVector pos = new PVector(rp.x, rp.y);
			allHandlePoints.add(pos);
			//parent.println("allHandlePoints: " + allHandlePoints.size());  //sanity check
		}
	}
	
	public ArrayList<PVector> getHandles()
	{
		return allHandlePoints;
	}
	
	
	public ArrayList<PVector> getPoints()
	{
		return allPoints;
	}

}
