package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;
import math.Transformation;
import math.Vector3d;

public class Plane extends GeometricObject {
	
	public final Transformation transformation;
	private final Vector3d reusableNormal = new Vector3d(0, 1, 0); 	//normal always points up the Y-axis
	private final Vector3d reusableInverseNormal = new Vector3d(0, -1, 0);
	
	public Plane(RGBColor color, Transformation transformation){
		super(color);
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		this.transformation = transformation;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		// t = (p0 - l0) . n / l . n
		Ray transformed = transformation.transformInverse(ray);			//inverse the ray
		double denom = reusableInverseNormal.dot(transformed.direction);
		if(denom > 1e-6){
			Vector3d vector = transformed.origin.scale(-1).toVector();
			double t = vector.dot(reusableInverseNormal) / denom;
			if(t >= 0){
				shadeRec.isHit = true;
				if(t < shadeRec.tMin){
					shadeRec.tMin = t;
					//calculate the hitpoint
					shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(t));
					//calculate the normal of the hitpoint
					double[] transformedNormal = transformation.getInverseTransformationMatrix().transpose().multiply(reusableNormal.toQuaternion());
					shadeRec.normal = new Vector3d(transformedNormal[0], transformedNormal[1], transformedNormal[2]);
					shadeRec.objectHit = this;
				}
			}
			return t >= 0;
		}
		return false;
	}

	@Override
	public boolean shadowIntersect(Ray ray, Point lightPosition) {
		Ray transformed = transformation.transformInverse(ray);			//inverse the ray
		double denom = reusableInverseNormal.dot(transformed.direction);
		if(denom > 1e-6){
			Vector3d vector = transformed.origin.scale(-1).toVector();
			double t = vector.dot(reusableInverseNormal) / denom;
			if(t >= 1e-6){
				double length1 = ray.origin.add(ray.direction.scale(t)).toVector().length();
				double length2 = lightPosition.subtract(ray.origin).length();
				if(length1 < length2)
					return true;
			}
		}
		return false;
	}

}
