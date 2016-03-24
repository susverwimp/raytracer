package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Plane extends GeometricObject {

	private Point3d pointInPlane;
	private Vector3d normal;

	public Plane(Material material){
		this(new Point3d(), new Vector3d(0, 1, 0), material);
	}
	
	public Plane(Point3d pointInPlane, Vector3d normal, Material material) {
		if(pointInPlane == null)
			throw new NullPointerException("the given point in the plane is null!");
		if(normal == null)
			throw new NullPointerException("the given normal is null!");
		this.material = material;
		this.pointInPlane = pointInPlane;
		this.normal = normal;
	}

	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double denom = ray.direction.dot(normal);
		if(Math.abs(denom) > kEpsilon){
			double t = pointInPlane.subtract(ray.origin).dot(normal) / denom;
			if(t > kEpsilon){
				shadeRec.t = t;
				shadeRec.normal = normal;
				shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(t));
				shadeRec.object = this;
				shadeRec.totalIntersections++;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		double denom = shadowRay.direction.dot(normal);
		if(Math.abs(denom) > kEpsilon){
			double t = pointInPlane.subtract(shadowRay.origin).dot(normal) / denom;
			if(t > kEpsilon){
				if(t<distance)
					return true;
			}
		}
		return false;
	}

}
