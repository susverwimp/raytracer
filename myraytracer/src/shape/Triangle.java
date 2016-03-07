package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Triangle extends GeometricObject {

	private Point3d v0;
	private Point3d v1;
	private Point3d v2;
	
	public Triangle(Point3d v0, Point3d v1, Point3d v2, Material material) {
		super(material);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		// compute plane's normal
	    Vector3d v0v1 = v1.subtract(v0); 
	    Vector3d v0v2 = v2.subtract(v0); 
	    // no need to normalize
	    Vector3d normal = v0v1.cross(v0v2); // normal
	    double denom = normal.dot(normal); 
	 
	    // Step 1: finding P
	 
	    // check if ray and plane are parallel ?
	    double normalDotRayDirection = normal.dot(ray.direction); 
	    if (Math.abs(normalDotRayDirection) < kEpsilon) // almost 0 
	        return false; // they are parallel so they don't intersect ! 
	 
	    // compute d parameter using equation 2
	    double d = normal.dot(v0.toVector()); 
	 
	    // compute t (equation 3)
	    double t = (normal.dot(ray.origin.toVector()) + d) / normalDotRayDirection; 
	    // check if the triangle is in behind the ray
	    if (t < 0) return false; // the triangle is behind 
	 
	    // compute the intersection point using equation 1
	    Point3d localHitPoint = ray.origin.add(ray.direction.scale(t)); 
	 
	    // Step 2: inside-outside test
	    Vector3d C; // vector perpendicular to triangle's plane 
	 
	    //barycentric values
	    double u,v;
	    
	    // edge 0
	    Vector3d edge0 = v1.subtract(v0); 
	    Vector3d vp0 = localHitPoint.subtract(v0); 
	    C = edge0.cross(vp0); 
	    if (normal.dot(C) < 0) return false; // P is on the right side 
	 
	    // edge 1
	    Vector3d edge1 = v2.subtract(v1); 
	    Vector3d vp1 = localHitPoint.subtract(v1); 
	    C = edge1.cross(vp1); 
	    if ((u = normal.dot(C)) < 0)  return false; // P is on the right side 
	 
	    // edge 2
	    Vector3d edge2 = v0.subtract(v2); 
	    Vector3d vp2 = localHitPoint.subtract(v2); 
	    C = edge2.cross(vp2); 
	    if ((v = normal.dot(C)) < 0) return false; // P is on the right side; 
	 
	    u /= denom; 
	    v /= denom; 
	    
	    shadeRec.t = t;
	    shadeRec.localHitPoint = localHitPoint;
	    shadeRec.normal = normal;
	 
	    return true; 
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		// compute plane's normal
	    Vector3d v0v1 = v1.subtract(v0); 
	    Vector3d v0v2 = v2.subtract(v0); 
	    // no need to normalize
	    Vector3d normal = v0v1.cross(v0v2); // normal
	 
	    // Step 1: finding P
	 
	    // check if ray and plane are parallel ?
	    double normalDotRayDirection = normal.dot(shadowRay.direction); 
	    if (Math.abs(normalDotRayDirection) < kEpsilon) // almost 0 
	        return false; // they are parallel so they don't intersect ! 
	 
	    // compute d parameter using equation 2
	    double d = normal.dot(v0.toVector()); 
	 
	    // compute t (equation 3)
	    double t = (normal.dot(shadowRay.origin.toVector()) + d) / normalDotRayDirection; 
	    // check if the triangle is in between the hitpoint and the light
	    if (t < 0 || t >= distance) return false; // the triangle is behind 
	 
	    // compute the intersection point using equation 1
	    Point3d localHitPoint = shadowRay.origin.add(shadowRay.direction.scale(t)); 
	 
	    // Step 2: inside-outside test
	    Vector3d C; // vector perpendicular to triangle's plane 
	 
	    // edge 0
	    Vector3d edge0 = v1.subtract(v0); 
	    Vector3d vp0 = localHitPoint.subtract(v0); 
	    C = edge0.cross(vp0); 
	    if (normal.dot(C) < 0) return false; // P is on the right side 
	 
	    // edge 1
	    Vector3d edge1 = v2.subtract(v1); 
	    Vector3d vp1 = localHitPoint.subtract(v1); 
	    C = edge1.cross(vp1); 
	    if (normal.dot(C) < 0)  return false; // P is on the right side 
	 
	    // edge 2
	    Vector3d edge2 = v0.subtract(v2); 
	    Vector3d vp2 = localHitPoint.subtract(v2); 
	    C = edge2.cross(vp2); 
	    if (normal.dot(C) < 0) return false; // P is on the right side; 
	 
	    return true; // this ray hits the triangle
	}
}