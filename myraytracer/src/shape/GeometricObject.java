package shape;

import java.util.Comparator;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import sampling.Sample;
import util.ShadeRec;

public abstract class GeometricObject implements Comparable<GeometricObject> {
	
	public Material material;
	public boolean shadows = true;
	public double invArea;
	public static final double kEpsilon = 1e-6;
	
	public static final Comparator<GeometricObject> xAxisComparator = new Comparator<GeometricObject>() {
		@Override
		public int compare(GeometricObject o1, GeometricObject o2) {
			if(o1.getCenter().x == o2.getCenter().x)
				return 0;
			if(o1.getCenter().x > o2.getCenter().x)
				return 1;
			else
				return -1;
		}
	};
	
	public static final Comparator<GeometricObject> yAxisComparator = new Comparator<GeometricObject>() {
		@Override
		public int compare(GeometricObject o1, GeometricObject o2) {
			if(o1.getCenter().y == o2.getCenter().y)
				return 0;
			if(o1.getCenter().y > o2.getCenter().y)
				return 1;
			else
				return -1;
		}
	};
	
	public static final Comparator<GeometricObject> zAxisComparator = new Comparator<GeometricObject>() {
		@Override
		public int compare(GeometricObject o1, GeometricObject o2) {
			if(o1.getCenter().z == o2.getCenter().z)
				return 0;
			if(o1.getCenter().z < o2.getCenter().z)
				return 1;
			else
				return -1;
		}
	};
	
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
	
	public Point3d getCenter(){
		return null;
	}
	
	@Override
	public int compareTo(GeometricObject o) {
		return 0;
	}
	
	public Point3d sample(){
		return null;
	}
	
	public Point3d sample(Sample samplePoint){
		return null;
	}
	
	public double pdf(ShadeRec shadeRec){
		return 1.0;
	}
	
	public Vector3d getNormal(Point3d p){
		return null;
	}
	
	public void setShadows(boolean shadows){
		this.shadows = shadows;
	}
	
}
