package bigscreensmilkasketch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import particleSystem.Particle;
import particleSystem.ParticleSystem;
import processing.core.*;
import vectorloader.VectorLoader;

public class ForegroundSketch extends Visualizer {

	float rotation;
	float spacing;
	int num;
	int totalParticles = 0;
	int freqLimit = 300;
	
	private PVector origin;
	private ArrayList<PVector> spiralCords = new ArrayList<PVector>();
	private ParticleSystem particleSystem;
	private ParticleSystem starSystem;
	
	private VectorLoader vector;
	private ArrayList<PVector> vectorPts;
	private ArrayList<PVector> vectorHandles;
	
	private String svgDir = "svg_files/";
	private ArrayList<String> svgs = new ArrayList<String>();
	
	private HashMap<Integer,ArrayList<PVector>> targetMap = new HashMap<Integer,ArrayList<PVector>>();
	private int targetIndex = 0;
	
	//Enivonmental Forces
	private boolean GRAVITY = false;
	private PVector gravity;
	
	float songSum;
	
	public ForegroundSketch(PApplet p) {
		super(p);
		rotation = 137.51f;
		spacing = 8;
		num = 1024;
		gravity = new PVector(0f, 0.5f);
		
		//Hacky hack hack to get all svg files
		Properties prop = System.getProperties();
		String path = prop.getProperty("java.class.path", null).split(":")[0].split("BigScreenHoubava")[0]+"BigScreenHoubava/";
		System.out.println(path+"svg_files");
		File dir = new File(path+"svg_files");
		
		for (String f:dir.list()){
			System.out.println(f);
			svgs.add(f);
		}
	}
	
	
	public void vizSetup() 
	{
		
		starSystem = initParticles(num,true);
		
		parent.noStroke();
		particleSystem = initParticles(num,false);
		createSprial(particleSystem);
		
		//the first set of target points is spiralCords
		targetMap.put(0,spiralCords);
		
		//create a new set of target points for each svg stored
		for (int i=0;i<svgs.size();i++) {
			ArrayList<PVector> newPts = attachNewTargetArray(svgDir+svgs.get(i));
			targetMap.put(i+1, newPts);
		}
	}
	
	public void vizDraw(String songDataString)
	{
		float[] songData = getDataAsArray(songDataString);
		localDraw(songData);
	}
	
	private void localDraw(float[] songData) 
	{
		
		parent.background(0);
		
		for (int i=0;i<songData.length;i++){
			songSum+=songData[i];
		}
		
		//gets a new set of particle targets every time a frequency limit gets hit
		if (songSum>freqLimit&&targetIndex<targetMap.size()-1){
			targetIndex++;	
			freqLimit+=200;
		}
		
		if (songSum>0) { //this will stop the drawing when the song stops; for some reason the sketch doesn't [yet] quit at the end
			for (int i=0;i<num;i++) {
	
				Particle star = starSystem.particles.get(i);
				int starSize = 10;
				int alphaBoost = 30;
				float songVal = songData[i%512];
				parent.fill(255,songVal*alphaBoost);
				parent.ellipse(star.getLocation().x, star.getLocation().y, starSize, starSize);	
				
				Particle p1 = particleSystem.particles.get(i);
				PVector target = targetMap.get(targetIndex).get(i);
				p1.arrive(target);
				if(GRAVITY) p1.applyForce(gravity);
				p1.update();
				
				parent.fill(197,246,252);
				parent.ellipse(p1.getLocation().x, p1.getLocation().y, 4, 4);
				
			}
			songSum=0;
		}
	}
	
	
	public ArrayList<PVector> attachNewTargetArray(String fileName) {
		vector = new VectorLoader(parent,fileName);
		vectorPts = vector.getPoints();
		vectorHandles = vector.getHandles();
		
		ArrayList<PVector> tempPts = new ArrayList<PVector>();
		int remainder = 0;
		int loops;
		
		//loop through the vector points until you have a list of 1024 (some of the vector pts will be repeated in the list)
		if (num>vectorPts.size()) {
			remainder = num % vectorPts.size();
			loops = (num-remainder)/vectorPts.size();
			while(loops>0){
				tempPts.addAll(vectorPts);
				loops--;
			}
			for (int j=0;j<remainder;j++){
				tempPts.add(vectorPts.get(j));				
			}
		} else { //i think this will work for the case when there are more vector pts than num; not yet tested though....
			for (int j=0;j<num;j++) {
				tempPts.add(vectorPts.get(j%vectorPts.size()));
			}
		}
		
		return tempPts;
	}
	
	private ParticleSystem initParticles(int numParticles, boolean ran)
	{
		ParticleSystem ps = new ParticleSystem(this);
		for(int i=0; i < numParticles; i++) {		    
		    PVector pv = new PVector(BigScreensMilkaSketch.mWidth/2, BigScreensMilkaSketch.mHeight/2);
		    if (ran){
		    	pv = new PVector(parent.random(0,BigScreensMilkaSketch.mWidth),
						parent.random(0,BigScreensMilkaSketch.mHeight));
		    }
		    Particle p = new Particle(this, pv);
		    ps.particles.add(p);
		    ps.totalNumOfParticles = totalParticles;
		}
		return ps;
	}
	
	
	private void createSprial(ParticleSystem ps)
	{
		for(int i=0; i < num; i++) {
		    Particle p  = ps.particles.get(i);
			//location
		    float radius = spacing * parent.sqrt(i);
		    float theta = i * parent.radians(rotation);
		    
		    float x = radius * parent.cos(theta);
		    float y = radius * parent.sin(theta);
		    int more = 100;
		    PVector pv = new PVector( (p.getLocation().x + x*more), (p.getLocation().y + y*more) );
		    spiralCords.add(pv);
		}
			
	}
	

	
	

}
