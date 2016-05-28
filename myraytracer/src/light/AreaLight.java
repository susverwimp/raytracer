package light;

import material.Material;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import shape.GeometricObject;
import util.ShadeRec;

public class AreaLight extends Light {

	public GeometricObject object;
	private Material material;

	public AreaLight(GeometricObject object) {
		this.object = object;
		this.material = object.material;
	}

	@Override
	public Vector3d getDirection(ShadeRec shadeRec) {
		// shadeRec.samplePoint = object.sample();
		shadeRec.samplePoint = object.sample(shadeRec.arealightSampler.getSampleUnitSquare());
		shadeRec.lightNormal = object.getNormal(shadeRec.samplePoint);
		shadeRec.wi = shadeRec.samplePoint.subtract(shadeRec.hitPoint).normalize();

		return shadeRec.wi;
	}

	@Override
	public RGBColor L(ShadeRec shadeRec) {
		double nDotD = shadeRec.lightNormal.scale(-1).dot(shadeRec.wi);
		if (nDotD > 0.0)
			return material.getLE(shadeRec).scale(object.invArea);
		return new RGBColor();
	}

	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec shadeRec) {
		double distance = (shadeRec.samplePoint.subtract(shadowRay.origin)).dot(shadowRay.direction);
		for (GeometricObject object : shadeRec.world.shapes) {
			if (object.shadowHit(shadowRay, distance))
				return true;
		}
		return false;
	}

	public double G(ShadeRec shadeRec) {
		double nDotD = shadeRec.lightNormal.scale(-1).dot(shadeRec.wi);
		double d2 = shadeRec.samplePoint.distanceSquared(shadeRec.hitPoint);

		return nDotD / d2;
	}

	public double pdf(ShadeRec shadeRec) {
		return object.pdf(shadeRec);
	}

}
