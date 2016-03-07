package tracer;

import math.RGBColor;
import math.Ray;

public abstract class Tracer {
	
	protected World world;
	
	public Tracer(World world){
		this.world = world;
	}
	
	public abstract RGBColor traceRay(Ray ray);

}
