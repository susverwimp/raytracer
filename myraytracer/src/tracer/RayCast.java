package tracer;

import math.RGBColor;
import math.Ray;
import util.ShadeRec;

public class RayCast extends Tracer {

	public RayCast(World world) {
		super(world);
	}

	@Override
	public RGBColor traceRay(Ray ray) {
		ShadeRec shadeRec = world.hitObjects(ray);
		if (shadeRec.isHit) {
			shadeRec.ray = ray;
			return shadeRec.object.material.shade(shadeRec);
		}
		return world.backgroundColor;
	}

}
