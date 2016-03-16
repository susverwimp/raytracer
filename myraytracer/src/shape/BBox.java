package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import util.ShadeRec;

public class BBox extends GeometricObject {

	public Point3d minPoint;
	public Point3d maxPoint;
	
	public BBox(Material material, Point3d minPoint, Point3d maxPoint) {
		super(material);
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;
		
		double a = 1.0 / ray.direction.x;
		if(a >= 0){
			txMin = (minPoint.x - ray.origin.x) * a;
			txMax = (maxPoint.x - ray.origin.x) * a;
		}
		else{
			txMin = (maxPoint.x - ray.origin.x) * a;
			txMax = (minPoint.x - ray.origin.x) * a;
		}
		
		double b = 1.0 / ray.direction.y;
		if(b>=0){
			tyMin = (minPoint.y - ray.origin.y) * b;
			tyMax = (maxPoint.y - ray.origin.y) * b;
		}
		else{
			tyMin = (maxPoint.y - ray.origin.y) * b;
			tyMax = (minPoint.y - ray.origin.y) * b;
		}
		
		double c = 1.0 / ray.direction.z;
		if(c>=0){
			tzMin = (minPoint.z - ray.origin.z) * c;
			tzMax = (maxPoint.z - ray.origin.z) * c;
		}
		else{
			tzMin = (maxPoint.z - ray.origin.z) * c;
			tzMax = (minPoint.z - ray.origin.z) * c;
		}
		
		double t0, t1;
		//find largest entering t value
		if(txMin > tyMin)
			t0 = txMin;
		else
			t0 = tyMin;
		if(tzMin > t0)
			t0 = tzMin;
		
		//find smallest exiting t value
		if(txMax < tyMax)
			t1 = txMax;
		else
			t1 = tyMax;
		if(tzMax < t1)
			t1 = tzMax;
		
		return (t0 < t1 && t1 > kEpsilon);
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		return false;
	}
	
}
