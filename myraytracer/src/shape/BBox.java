package shape;

import math.Point3d;
import math.Ray;
import util.ShadeRec;

public class BBox extends GeometricObject {

	public Point3d minPoint;
	public Point3d maxPoint;
	
	public BBox(Point3d minPoint, Point3d maxPoint) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
	}
	
	public BBox(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax){
		this(new Point3d(xMin, yMin, zMin), new Point3d(xMax, yMax, zMax));
	}
	
	public BBox getBoundingBox(){
		return this;
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
		if(c<=0){
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
		
		if(t0 < t1 && t1 > kEpsilon){
			if(t0 > kEpsilon)
				shadeRec.t = t0;
			else
				shadeRec.t = t1;
			shadeRec.totalIntersections++;
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;
		
		double a = 1.0 / shadowRay.direction.x;
		if(a >= 0){
			txMin = (minPoint.x - shadowRay.origin.x) * a;
			txMax = (maxPoint.x - shadowRay.origin.x) * a;
		}
		else{
			txMin = (maxPoint.x - shadowRay.origin.x) * a;
			txMax = (minPoint.x - shadowRay.origin.x) * a;
		}
		
		double b = 1.0 / shadowRay.direction.y;
		if(b>=0){
			tyMin = (minPoint.y - shadowRay.origin.y) * b;
			tyMax = (maxPoint.y - shadowRay.origin.y) * b;
		}
		else{
			tyMin = (maxPoint.y - shadowRay.origin.y) * b;
			tyMax = (minPoint.y - shadowRay.origin.y) * b;
		}
		
		double c = 1.0 / shadowRay.direction.z;
		if(c<=0){
			tzMin = (minPoint.z - shadowRay.origin.z) * c;
			tzMax = (maxPoint.z - shadowRay.origin.z) * c;
		}
		else{
			tzMin = (maxPoint.z - shadowRay.origin.z) * c;
			tzMax = (minPoint.z - shadowRay.origin.z) * c;
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
		
		if(t0 < t1 && t1 > kEpsilon && t0 < distance){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return "minPoint: " + minPoint + " maxPoint: " + maxPoint;
	}
}
