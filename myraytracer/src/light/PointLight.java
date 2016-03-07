package light;

import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import shape.GeometricObject;
import util.ShadeRec;

public class PointLight extends Light {

	private double ls;
	private RGBColor color;
	private Point3d location;
	
	public PointLight() {
		this(1.0, new RGBColor(1, 1, 1), new Point3d());
	}
	
	public PointLight(double ls, RGBColor color, Point3d location){
		this.ls = ls;
		this.color = color;
		this.location = location;
	}
	
	@Override
	public Vector3d getDirection(ShadeRec shadeRec) {
		return location.subtract(shadeRec.localHitPoint).normalize();
	}

	@Override
	public RGBColor L(ShadeRec shadeRec) {
		return color.scale(ls);
	}

	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec shadeRec) {
		double distance = location.subtract(shadowRay.origin).length();
		for(GeometricObject object : shadeRec.world.shapes){
			if(object.shadowHit(shadowRay, distance))
				return true;
		}
		return false;
	}

}
