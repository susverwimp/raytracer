package shape;

import material.Material;
import math.Ray;
import util.ShadeRec;

public abstract class GeometricObject {
	
	public Material material;
	public static final double kEpsilon = 1e-6;
	
	public GeometricObject(Material material){
		this.material = material;
	}
	
	public abstract boolean intersect(Ray ray, ShadeRec shadeRec);
	public abstract boolean shadowHit(Ray shadowRay, double distance);
	
	
}
