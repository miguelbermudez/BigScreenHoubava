package bigscreensmilkasketch;

import processing.core.*;


public class Note extends BigScreensMilkaSketch
{
	int c;
	float x;
	float y;
	float w;
	float h;
	PApplet parent;
	
	Note(PApplet p, int t_c, float t_w, float t_h) {
	    parent = p;
	    
	    c = t_c;
	    x = parent.random(100,mWidth-100);
	    y = parent.random(50,mHeight-50);
	    w = t_w;
	    h = t_h;
	  }
	
	public void update(int t_c, float t_w, float t_h) 
	{
		c = t_c;

	    w = t_w;
	    h = t_h;
    }
	
	public void render() 
	{
		parent.fill(c);
		parent.ellipse(x,y,w,h);
	}
}
