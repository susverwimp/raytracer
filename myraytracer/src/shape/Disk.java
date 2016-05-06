package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Disk extends GeometricObject {

	private Point3d center = new Point3d();
	private Vector3d normal = new Vector3d();
	private double radiusSquared;
	
	public Disk(Point3d center, Vector3d normal, double radius, Material material){
		this.center = center;
		this.normal = normal;
		radiusSquared = radius * radius;
		this.material = material;
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double t = center.subtract(ray.origin.scale(-1)).dot(normal) / (ray.direction.dot(normal));
		
		if(t <= kEpsilon)
			return false;
		
		Point3d p = ray.origin.add(ray.direction.scale(t));
		
		if(center.distanceSquared(p)< radiusSquared){
			shadeRec.t = t;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = p;
			shadeRec.object = this;
			shadeRec.totalIntersections++;
			return true;
		}
		return false;
	}

}
