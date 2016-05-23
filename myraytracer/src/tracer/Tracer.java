package tracer;

import math.RGBColor;
import math.Ray;
import sampling.Sampler;
import world.World;

public abstract class Tracer {
	
	protected World world;
	
	public Tracer(World world){
		this.world = world;
	}
	
	public abstract RGBColor traceRay(Ray ray, Sampler sampler, int depth);

}
