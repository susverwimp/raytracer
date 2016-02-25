package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;
import math.Vector3d;

public class Triangle extends GeometricObject {

	private Point v0;
	private Point v1;
	private Point v2;
	private Vector3d normal;
	
	public Triangle(RGBColor color, Point v0, Point v1, Point v2) {
		this(color, v0, v1, v2, null);
	}
	
	public Triangle(RGBColor color, Point v0, Point v1, Point v2, Vector3d normal){
		super(color);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.normal = normal;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		// compute plane's normal
		Vector3d v0Tov1 = v1.subtract(v0);
		Vector3d v0Tov2 = v2.subtract(v0);
		if(normal == null)
			normal = v0Tov1.cross(v0Tov2).normalize();
		
	    double area2 = normal.length(); 
	 
	    // Step 1: finding P
	    // check if ray and plane are parallel ?
	    double normalDotRayDirection = normal.dot(ray.direction); 
	    if (normalDotRayDirection < 1e-6) // almost 0 
	        return false; // they are parallel so they don't intersect ! 
	 
	    // compute d parameter using equation 2
	    double d = normal.dot(v0.toVector()); 
	 
	    // compute t (equation 3)
	    double t = (normal.dot(ray.origin.toVector()) + d) / normalDotRayDirection; 
	    // check if the triangle is in behind the ray
	    if (t < 0) return false; // the triangle is behind 
	 
	    if(t < shadeRec.tMin){
		    // compute the intersection point using equation 1
		    Point P = ray.origin.add(ray.direction.scale(t)); 
		 
		    // Step 2: inside-outside test
		    Vector3d C; // vector perpendicular to triangle's plane 
		 
		    // edge 0
		    Vector3d edge0 = v1.subtract(v0); 
		    Vector3d vp0 = P.subtract(v0); 
		    C = edge0.cross(vp0); 
		    if (normal.dot(C) < 0) return false; // P is on the right side 
		 
		    // edge 1
		    Vector3d edge1 = v2.subtract(v1); 
		    Vector3d vp1 = P.subtract(v1); 
		    C = edge1.cross(vp1); 
		    if (normal.dot(C) < 0)  return false; // P is on the right side 
		 
		    // edge 2
		    Vector3d edge2 = v0.subtract(v2); 
		    Vector3d vp2 = P.subtract(v2); 
		    C = edge2.cross(vp2); 
		    if (normal.dot(C) < 0) return false; // P is on the right side; 
		 
		    shadeRec.tMin = t;
		    shadeRec.isHit = true;
		    shadeRec.localHitPoint = P;
		    shadeRec.normal = normal;
		    
		    return true; // this ray hits the triangle 
	    }
	    return false;
	}

	@Override
	public boolean shadowIntersect(Ray ray, Point lightPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
