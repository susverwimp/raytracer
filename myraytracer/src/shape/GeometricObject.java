package shape;

import material.Material;
import math.Ray;
import util.ShadeRec;

public abstract class GeometricObject {
	
	public Material material;
	public static final double kEpsilon = 1e-6;
	
	public BBox getBoundingBox(){
		return null;
	}
	
	public abstract boolean intersect(Ray ray, ShadeRec shadeRec);
	public boolean shadowHit(Ray shadowRay, double distance){
		return false;
	}
	
	public void setMaterial(Material material){
		this.material = material;
	}
	
	
}
