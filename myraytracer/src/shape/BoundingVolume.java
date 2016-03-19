package shape;

import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class BoundingVolume extends Compound {
	
	public BBox boundingBox;
	
	public BoundingVolume(){
		
	}
	
	public void calculateHierarchy(){
		Point3d p0 = getMinCoordinates();
		Point3d p1 = getMaxCoordinates();
		boundingBox = new BBox(p0, p1);
		System.out.println(boundingBox);
	}
	
	public Point3d getMinCoordinates(){
		BBox bbox;
		Point3d p0 = new Point3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		
		for(GeometricObject object : objects){
			bbox = object.getBoundingBox();
			if(bbox.minPoint.x < p0.x)
				p0.x = bbox.minPoint.x;
			if(bbox.minPoint.y < p0.y)
				p0.y = bbox.minPoint.y;
			if(bbox.minPoint.z > p0.z)
				p0.z = bbox.minPoint.z;
		}
		
		p0.x -= kEpsilon;
		p0.y -= kEpsilon;
		p0.z += kEpsilon;
		
		return p0;
	}
	
	public Point3d getMaxCoordinates(){
		BBox bbox;
		Point3d p0 = new Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		
		for(GeometricObject object : objects){
			bbox = object.getBoundingBox();
			if(bbox.maxPoint.x > p0.x)
				p0.x = bbox.maxPoint.x;
			if(bbox.maxPoint.y > p0.y)
				p0.y = bbox.maxPoint.y;
			if(bbox.maxPoint.z < p0.z)
				p0.z = bbox.maxPoint.z;
		}
		
		p0.x += kEpsilon;
		p0.y += kEpsilon;
		p0.z -= kEpsilon;
		
		return p0;
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		if(boundingBox.intersect(ray, shadeRec)){
			Vector3d normal = new Vector3d();
			Point3d localHitPoint = new Point3d();
			double tmin = Double.POSITIVE_INFINITY;
			for (GeometricObject object : objects) {
				if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
					shadeRec.isHit = true;
					tmin = shadeRec.t;
					material = object.material;
					normal = shadeRec.normal;
					localHitPoint = shadeRec.localHitPoint;
				}
			}
			if (shadeRec.isHit) {
				shadeRec.t = tmin;
				shadeRec.normal = normal;
				shadeRec.localHitPoint = localHitPoint;
			}
			
//			return shadeRec.isHit;
			return true;
		}
		return false;
	}
	
//	@Override
//	public boolean shadowHit(Ray shadowRay, double distance) {
//		return false;
//	}

}
