package tracer;

import java.util.Random;

import material.Emissive;
import math.RGBColor;
import math.Ray;
import sampling.PureRandom;
import sampling.Sampler;
import util.ShadeRec;
import world.World;

public class HybridPathTracing extends Tracer {

	public HybridPathTracing(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray, Sampler sampler, int depth) {
		if(depth < World.MAX_DEPTH){
			ShadeRec shadeRec = world.hitObjects(ray);
			if(shadeRec.isHit){
				if(!(shadeRec.object.material instanceof Emissive) || depth == 0){
					shadeRec.materialSampler = sampler;
					shadeRec.arealightSampler = new PureRandom(1, 1, new Random().nextLong());
					shadeRec.depth = depth;
					shadeRec.ray = ray;
					
					return shadeRec.object.material.hybridPathShade(shadeRec);
				}
			}
		}
		return World.BACKGROUND_COLOR;
	}

}
