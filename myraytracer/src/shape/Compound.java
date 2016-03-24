package shape;

import java.util.ArrayList;
import java.util.List;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Compound extends GeometricObject {

	public List<GeometricObject> objects = new ArrayList<>();

	public void addObject(GeometricObject object){
		objects.add(object);
	}
	
	public void setMaterial(Material material){
		for(GeometricObject object : objects){
			object.setMaterial(material);
		}
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Vector3d normal = new Vector3d();
		Point3d localHitPoint = new Point3d();
		double tmin = Double.POSITIVE_INFINITY;
		for (GeometricObject object : objects) {
			if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
				shadeRec.isHit = true;
				tmin = shadeRec.t;
				material = shadeRec.object.material;
				normal = shadeRec.normal;
				localHitPoint = shadeRec.localHitPoint;
			}
		}
		if (shadeRec.isHit) {
			shadeRec.t = tmin;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = localHitPoint;
		}
		
		return shadeRec.isHit;
	}
	
	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		for(GeometricObject object : objects){
			if(object.shadowHit(shadowRay, distance)){
				return true;
			}
		}
		return false;
	}

}
