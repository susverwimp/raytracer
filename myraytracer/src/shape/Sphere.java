package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Transformation;
import math.Vector3d;
import util.ShadeRec;

/**
 * Represents a three-dimensional {@link Sphere} with radius one and centered at
 * the origin, which is transformed by the given {@link Transformation}.
 * 
 * @author Niels Billen
 * @version 0.2
 */
public class Sphere extends GeometricObject {

	/**
	 * Creates a new unit {@link Sphere} at the origin, transformed by the given
	 * {@link Transformation}.
	 * 
	 */
	public Sphere(Material material) {
		this.material = material;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shape.Shape#intersect(geometry3d.Ray3D)
	 */
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Vector3d o = ray.origin.toVector();

		double a = ray.direction.dot(ray.direction);
		double b = 2.0 * (ray.direction.dot(o));
		double c = o.dot(o) - 1.0;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return false;
		double dr = Math.sqrt(d);

		// numerically solve the equation a*t^2 + b * t + c = 0
		double q = -0.5 * (b < 0 ? (b - dr) : (b + dr));

		double t0 = q / a;
		double t1 = c / q;
		
		if (t1 < t0) {
			double temp = t1;
			t1 = t0;
			t0 = temp;
		}

		if(t0 >= kEpsilon || t1 >= kEpsilon){
			if (t0 < 0) {
				t0 = t1;
			}
			shadeRec.t = t0;
			// calculate the hitpoint
			shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(t0));
			// calculate the normal of the hitpoint
			shadeRec.normal = shadeRec.localHitPoint.toVector();
			shadeRec.totalIntersections++;
			shadeRec.object = this;
			return true;
		}
		return false;
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		if(!shadows)
			return false;
		Vector3d o = shadowRay.origin.toVector();

		double a = shadowRay.direction.dot(shadowRay.direction);
		double b = 2.0 * (shadowRay.direction.dot(o));
		double c = o.dot(o) - 1.0;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return false;
		double dr = Math.sqrt(d);

		// numerically solve the equation a*t^2 + b * t + c = 0
		double q = -0.5 * (b < 0 ? (b - dr) : (b + dr));

		double t0 = q / a;
		double t1 = c / q;
		
		if (t1 < t0) {
			double temp = t1;
			t1 = t0;
			t0 = temp;
		}

		if(t0 >= kEpsilon || t1 >= kEpsilon){
			if (t0 < 0) {
				t0 = t1;
			}
			if(t0 < distance)
				return true;
		}
		return false;
	}
	
	public BBox getBoundingBox() {
		return new BBox(-1, -1, 1, 1, 1, -1);
	};
	
	@Override
	public Point3d getCenter(){
		return new Point3d();
	}
}
