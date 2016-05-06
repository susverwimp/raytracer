package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Transformation;
import util.ShadeRec;

public class Instance extends GeometricObject {

	public boolean transformTexture;
	public GeometricObject object;
	public Transformation transformation;

	public Instance(GeometricObject object, boolean transformTexture, Transformation transformation,
			Material material) {
		this.object = object;
		this.transformTexture = transformTexture;
		this.transformation = transformation;
		this.material = material;
	}

	public BBox getBoundingBox() {
		double delta = 0;
		BBox bbox = object.getBoundingBox();
//		double xMin = bbox.minPoint.x;
//		double xMax = bbox.maxPoint.x;
//		double yMin = bbox.minPoint.y;
//		double yMax = bbox.maxPoint.y;
//		double zMin = bbox.minPoint.z;
//		double zMax = bbox.maxPoint.z;
		
		
		//get all vertices of the childs bounding box and transform them
		//then see which is the maximum and the minimum point
//		Point3d p1 = 
		bbox.minPoint = transformation.transform(bbox.minPoint);
		bbox.maxPoint = transformation.transform(bbox.maxPoint);
		if (bbox.minPoint.x > bbox.maxPoint.x) {
			double temp = bbox.minPoint.x;
			bbox.minPoint.x = bbox.maxPoint.x;
			bbox.maxPoint.x = temp;
		}
		if (bbox.minPoint.y > bbox.maxPoint.y) {
			double temp = bbox.minPoint.y;
			bbox.minPoint.y = bbox.maxPoint.y;
			bbox.maxPoint.y = temp;
		}
		if (bbox.minPoint.z < bbox.maxPoint.z) {
			double temp = bbox.minPoint.z;
			bbox.minPoint.z = bbox.maxPoint.z;
			bbox.maxPoint.z = temp;
		}
		
		bbox.minPoint.x -= delta;
		bbox.minPoint.y -= delta;
		bbox.minPoint.z += delta;
		bbox.maxPoint.x += delta;
		bbox.maxPoint.y += delta;
		bbox.maxPoint.z -= delta;
		
		return bbox;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Ray transformedRay = transformation.transformInverse(ray);
		if (object.intersect(transformedRay, shadeRec)) {
			shadeRec.normal = transformation.getInverseTransformationMatrix().transpose().transform(shadeRec.normal)
					.normalize();
			if (!transformTexture)
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

	@Override
	public Point3d getCenter() {
		return transformation.transform(object.getCenter());
	}
	
	@Override
	public String toString(){
		return getCenter().toString();
	}
}
