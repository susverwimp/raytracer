package light;

import material.Material;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import shape.GeometricObject;
import util.ShadeRec;

public class AreaLight extends Light {

	private GeometricObject object;
	private Material material;
	private Point3d samplePoint;
	private Vector3d lightNormal;
	private Vector3d wi;
	
	public AreaLight(GeometricObject object){
		this.object = object;
		this.material = object.material;
	}
	
	@Override
	public Vector3d getDirection(ShadeRec shadeRec) {
		samplePoint = object.sample();
		lightNormal = object.getNormal(samplePoint);
		wi = samplePoint.subtract(shadeRec.hitPoint);
		wi.normalize();
		
		return wi;
	}

	@Override
	public RGBColor L(ShadeRec shadeRec) {
		double nDotD = lightNormal.scale(-1).dot(wi);
		if(nDotD > 0.0)
			return material.getLE(shadeRec);
		return new RGBColor();
	}

	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec shadeRec) {
		double distance = (samplePoint.subtract(shadowRay.origin)).dot(shadowRay.direction);
		for(GeometricObject object : shadeRec.world.shapes){
			if(object.shadowHit(shadowRay, distance))
				return true;
		}
		return false;
	}
	
	public double G(ShadeRec shadeRec){
		double nDotD = lightNormal.scale(-1).dot(wi);
		double d2 = samplePoint.distanceSquared(shadeRec.hitPoint);
		
		return nDotD / d2;
	}
	
	public double pdf(ShadeRec shadeRec){
		return object.pdf(shadeRec);
	}

}
