package shape;

import material.Emissive;
import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import sampling.Sample;
import util.ShadeRec;

public class Rectangle extends GeometricObject {

	private Point3d p0;
	private Vector3d a;
	private Vector3d b;
	private Vector3d normal;
	
	private double aLengthSquared;
	private double bLengthSquared;
	
	public Rectangle(Point3d p0, Vector3d a, Vector3d b, Vector3d normal, Material material) {
		this.p0 = p0;
		this.a=a;
		this.b=b;
		this.normal = normal.normalize();
		this.material = material;
		aLengthSquared = a.lengthSquared();
		bLengthSquared = b.lengthSquared();
		invArea = (1.0 / (a.length() * b.length()));
		if(material instanceof Emissive)
			((Emissive)material).setObject(this);
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double t = (p0.subtract(ray.origin)).dot(normal) / (ray.direction.dot(normal));
		
		if(t <= kEpsilon)
			return false;
		
		Point3d p = ray.origin.add(ray.direction.scale(t));
		Vector3d d = p.subtract(p0);
		
		double dDotA = d.dot(a);
		if(dDotA < 0.0 || dDotA > aLengthSquared)
			return false;
		
		double dDotB = d.dot(b);
		if(dDotB < 0.0 || dDotB > bLengthSquared)
			return false;
		
		shadeRec.t = t;
		shadeRec.object = this;
		shadeRec.localHitPoint = p;
		shadeRec.normal = normal;
		shadeRec.totalIntersections++;
		return true;
	}
	
	@Override
	public boolean shadowHit(Ray shadowRay, double distance){
		if(!shadows)
			return false;
		double t = (p0.subtract(shadowRay.origin)).dot(normal) / (shadowRay.direction.dot(normal));
		
		if(t <= kEpsilon || t > distance - kEpsilon)
			return false;
		
		Point3d p = shadowRay.origin.add(shadowRay.direction.scale(t));
		Vector3d d = p.subtract(p0);
		
		double dDotA = d.dot(a);
		if(dDotA < 0.0 || dDotA > aLengthSquared)
			return false;
		
		double dDotB = d.dot(b);
		if(dDotB < 0.0 || dDotB > bLengthSquared)
			return false;
		
		return true;
	}
	
	@Override
	public Point3d sample(Sample samplePoint){
		return (p0.add(a.scale(samplePoint.x)).add(b.scale(samplePoint.y)));
	}
	
	@Override
	public double pdf(ShadeRec shadeRec){
		return invArea;
	}
	
	@Override
	public Vector3d getNormal(Point3d p){
		return normal;
	}

}
