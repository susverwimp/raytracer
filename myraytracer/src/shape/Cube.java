package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;
import math.Transformation;
import math.Vector3d;
import utils.Util;

public class Cube extends GeometricObject {

	public final Point minPoint;
	public final Point maxPoint;
	
	public Cube(RGBColor color,Point minPoint, Point maxPoint){
		super(color);
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double tmin = (minPoint.x - ray.origin.x) / ray.direction.x; 
		double tmax = (maxPoint.x - ray.origin.x) / ray.direction.x; 
	 
	    if (tmin > tmax) Util.swap(tmin, tmax); 
	 
	    double tymin = (minPoint.y - ray.origin.y) / ray.direction.y; 
	    double tymax = (maxPoint.y - ray.origin.y) / ray.direction.y; 
	 
	    if (tymin > tymax) Util.swap(tymin, tymax); 
	 
	    if ((tmin > tymax) || (tymin > tmax)) 
	        return false; 
	 
	    if (tymin > tmin) 
	        tmin = tymin; 
	 
	    if (tymax < tmax) 
	        tmax = tymax; 
	 
	    double tzmin = (minPoint.z - ray.origin.z) / ray.direction.z; 
	    double tzmax = (maxPoint.z - ray.origin.z) / ray.direction.z; 
	 
	    if (tzmin > tzmax) Util.swap(tzmin, tzmax); 
	 
	    if ((tmin > tzmax) || (tzmin > tmax)) 
	        return false; 
	 
	    if (tzmin > tmin) 
	        tmin = tzmin; 
	 
	    if (tzmax < tmax) 
	        tmax = tzmax; 
	 
	    shadeRec.isHit = true;
	    shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(tmin));
	    shadeRec.normal = new Vector3d(0,0,-1);
	    shadeRec.objectHit = this;
	    return true;
	}

	@Override
	public boolean shadowIntersect(Ray ray, Point lightPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
