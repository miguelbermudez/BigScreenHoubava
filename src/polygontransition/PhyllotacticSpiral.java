/**
 * 
 */
package polygontransition;

import processing.core.*;

/**
 * @author Miguel Bermudez
 *
 */
public class PhyllotacticSpiral 
{
	PApplet parent;
	float rotation;
	float spacing;
	int num;
	PVector origin;
	PVector[] spiralPoints;
	
	
	public PhyllotacticSpiral(PApplet p, PVector _origin)
	{
		parent = p;
		
		rotation = 137.51f;
		
		spacing = 8;
		num = 1020;
		
		origin = _origin;
	}
	
	public PVector[] values() 
	{

		spiralPoints = new PVector[num];
		for(int i=0; i < num; i++) {
		    //location
		    float radius = spacing * parent.sqrt(i);
		    float theta = i * parent.radians(rotation);
		    
		    float x = radius * parent.cos(theta);
		    float y = radius * parent.sin(theta);
		    
		    PVector pv = new PVector(origin.x + x, origin.y + y);		    
		    spiralPoints[i] = pv;
		}
		
		return spiralPoints;
	}

}
