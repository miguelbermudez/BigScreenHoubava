package bigscreensmilkasketch;

import processing.core.*;


public class Note 
{
	int c;
	float x;
	float y;
	float w;
	float h;
	PApplet parent;
	
	Note(PApplet p, int t_c, float t_x, float t_y, float t_w, float t_h) {
	    parent = p;
	    
		c = t_c;
	    //x = t_x;
	    //y = t_y;
	    x = parent.random(100,parent.width-100);
	    y = parent.random(50,parent.height-50);
	    w = t_w;
	    h = t_h;
	  }
	
	public void update(int t_c, float t_x, float t_y, float t_w, float t_h) 
	{
		c = t_c;
		// x = t_x;
	    //y = t_y;
	    w = t_w;
	    h = t_h;
    }
	
	public void render() 
	{
		parent.fill(c);
		parent.ellipse(x,y,w,h);
	}
}
