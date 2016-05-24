package tracer;

import math.RGBColor;
import math.Ray;
import sampling.Sampler;
import util.ShadeRec;
import world.World;

public class HybridPathTracing extends Tracer {

	public HybridPathTracing(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray, Sampler sampler, int depth) {
		if(depth <= World.MAX_DEPTH){
			ShadeRec shadeRec = world.hitObjects(ray);
			if(shadeRec.isHit){
//				Sampler materialSampler = new PureRandom(World.BRANCHING_FACTOR, 1, new Random().nextLong());
//				materialSampler.mapSamplesToCosineHemisphere();
				shadeRec.materialSampler = sampler;
				
				shadeRec.depth = depth;
				shadeRec.ray = ray;
				
				return shadeRec.object.material.pathShade(shadeRec);
			}
		}
		return World.BACKGROUND_COLOR;
	}

}
