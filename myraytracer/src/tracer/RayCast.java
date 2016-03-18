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
			if (World.SHOW_BVH) {
				if(shadeRec.totalIntersections == 1)
					return World.falseColor1;
				else if(shadeRec.totalIntersections == 2)
					return World.falseColor2;
				else
					return new RGBColor(0,0,0);
			} else {
				shadeRec.ray = ray;
				return shadeRec.material.shade(shadeRec);
			}
		}
		return world.backgroundColor;
	}

}
