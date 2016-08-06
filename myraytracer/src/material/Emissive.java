package material;

import math.RGBColor;
import shape.GeometricObject;
import util.ShadeRec;
import world.World;

public class Emissive extends Material {

	public double p;
	public RGBColor ce = new RGBColor();
	public GeometricObject object;

	public void setPower(double p) {
		this.p = p;
	}

	public void setCE(double r, double g, double b) {
		ce.r = r;
		ce.g = g;
		ce.b = b;
	}
	
	public void setObject(GeometricObject object){
		this.object = object;
	}

	public RGBColor getLE(ShadeRec shadeRec) {
		return ce.scale(p * object.invArea / Math.PI);
	}

	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		return null;
	}

	@Override
	public RGBColor areaLightShade(ShadeRec shadeRec) {
		if (shadeRec.normal.scale(-1).dot(shadeRec.ray.direction) > 0.0) {
//			return ce.scale(p * object.invArea);
			return getLE(shadeRec);
		}
		return World.BACKGROUND_COLOR;
	}

	@Override
	public RGBColor pathShade(ShadeRec shadeRec) {
		if (shadeRec.depth == World.SHOW_BOUNCE || World.SHOW_BOUNCE == -1)
			if (shadeRec.normal.scale(-1).dot(shadeRec.ray.direction) > 0.0){
				System.out.println(getLE(shadeRec));
				return getLE(shadeRec);
			}
//				return ce.scale(p * object.invArea / Math.PI);
		return World.BACKGROUND_COLOR;
	}

	@Override
	public RGBColor hybridPathShade(ShadeRec shadeRec) {
		if (shadeRec.depth == World.SHOW_BOUNCE || World.SHOW_BOUNCE == -1)
				 if(shadeRec.normal.scale(-1).dot(shadeRec.ray.direction) >
				 0.0 && (shadeRec.depth == 0 ||
				 shadeRec.ray.originatingMaterial instanceof SVReflective))
//				return ce.scale(p * object.invArea);
					 return getLE(shadeRec);
		return World.BACKGROUND_COLOR;
	}

}
