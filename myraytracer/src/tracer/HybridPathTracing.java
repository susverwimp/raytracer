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
	public RGBColor traceRay(Ray ray, Sampler arealightSampler, Sampler materialSampler, int depth) {
		if (depth <= World.MAX_BOUNCES) {
			ShadeRec shadeRec = world.hitObjects(ray);
			if (shadeRec.isHit) {
				shadeRec.materialSampler = materialSampler;
				shadeRec.arealightSampler = arealightSampler;
				shadeRec.depth = depth;
				shadeRec.ray = ray;

				return shadeRec.object.material.hybridPathShade(shadeRec);
			}
		}
		return World.BACKGROUND_COLOR;
	}

}
