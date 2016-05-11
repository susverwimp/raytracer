package tracer;

import math.RGBColor;
import math.Ray;
import sampling.Regular;
import sampling.Sampler;
import util.ShadeRec;

public class AreaLighting extends Tracer {

	public AreaLighting(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray, Sampler arealightSampler) {
		ShadeRec shadeRec = world.hitObjects(ray);
		
		if(shadeRec.isHit){
			shadeRec.ray = ray;
			shadeRec.arealightSampler = arealightSampler;
			return shadeRec.object.material.areaLightShade(shadeRec);
		}
		return world.backgroundColor;
	}

}
