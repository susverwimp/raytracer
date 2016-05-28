package tracer;

import math.RGBColor;
import math.Ray;
import sampling.Sampler;
import util.ShadeRec;
import world.World;

public class PathTracer extends Tracer {

	public PathTracer(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray, Sampler arealightSampler, Sampler materialSampler, int depth) {
		if(depth <= World.MAX_BOUNCES){
			ShadeRec shadeRec = world.hitObjects(ray);
			if(shadeRec.isHit){
//				Sampler materialSampler = new PureRandom(World.BRANCHING_FACTOR, 1, new Random().nextLong());
//				materialSampler.mapSamplesToCosineHemisphere();
				shadeRec.materialSampler = materialSampler;
				
				shadeRec.depth = depth;
				shadeRec.ray = ray;
				
				return shadeRec.object.material.pathShade(shadeRec);
			}
		}
		return World.BACKGROUND_COLOR;
	}

}
