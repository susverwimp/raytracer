package tracer;

import java.util.Random;

import math.RGBColor;
import math.Ray;
import sampling.PureRandom;
import sampling.Sampler;
import util.ShadeRec;

public class PathTracer extends Tracer {

	public PathTracer(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray, Sampler arealightSampler, int depth, int seed) {
		if(depth <= World.MAX_DEPTH){
			ShadeRec shadeRec = world.hitObjects(ray);
			if(shadeRec.isHit){
				Sampler materialSampler = new PureRandom(World.BRANCHING_FACTOR, 1, new Random().nextInt());
				materialSampler.mapSamplesToCosineHemisphere();
				shadeRec.materialSampler = materialSampler;
				
				shadeRec.depth = depth;
				shadeRec.ray = ray;
				
				return shadeRec.object.material.pathShade(shadeRec);
			}
		}
		return World.BACKGROUND_COLOR;
	}

}
