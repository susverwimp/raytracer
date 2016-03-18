package shape;

import material.Material;
import math.Ray;
import math.Transformation;
import util.ShadeRec;

public class Instance extends GeometricObject {

	public boolean transformTexture;
	public GeometricObject object;
	public Transformation transformation;
	
	public Instance(GeometricObject object, boolean transformTexture, Transformation transformation, Material material) {
		this.object = object;
		this.transformTexture = transformTexture;
		this.transformation = transformation;
		this.material = material;
	}
	
	public BBox getBoundingBox(){
		BBox bbox = object.getBoundingBox();
		bbox.minPoint = transformation.transform(bbox.minPoint);
		bbox.maxPoint = transformation.transform(bbox.maxPoint);
		return object.getBoundingBox();
		
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Ray transformedRay = transformation.transformInverse(ray);
		if(object.intersect(transformedRay, shadeRec)){
			shadeRec.normal = transformation.getInverseTransformationMatrix().transpose().transform(shadeRec.normal).normalize();
			if(object.material != null)
				material = object.material;
			if(!transformTexture)
				shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(shadeRec.t));
			return true;
		}
		return false;
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		Ray transformedRay = transformation.transformInverse(shadowRay);
		return object.shadowHit(transformedRay, distance);
	}

}
