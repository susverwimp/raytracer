package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;
import math.Transformation;
import math.Vector3d;

/**
 * Represents a three-dimensional {@link Sphere} with radius one and centered at
 * the origin, which is transformed by the given {@link Transformation}.
 * 
 * @author Niels Billen
 * @version 0.2
 */
public class Sphere extends GeometricObject {
	public final Transformation transformation;

	/**
	 * Creates a new unit {@link Sphere} at the origin, transformed by the given
	 * {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Sphere}.
	 * @throws NullPointerException
	 *             when the transformation is null.
	 */
	public Sphere(RGBColor color, Transformation transformation) {
		super(color);
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		this.transformation = transformation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shape.Shape#intersect(geometry3d.Ray3D)
	 */
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Ray transformed = transformation.transformInverse(ray);

		Vector3d o = transformed.origin.toVector();

		double a = transformed.direction.dot(transformed.direction);
		double b = 2.0 * (transformed.direction.dot(o));
		double c = o.dot(o) - 1.0;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return false;
		double dr = Math.sqrt(d);

		// numerically solve the equation a*t^2 + b * t + c = 0
		double q = -0.5 * (b < 0 ? (b - dr) : (b + dr));

		double t0 = q / a;
		double t1 = c / q;

		if (t1 < t0) {
			double temp = t1;
			t1 = t0;
			t0 = temp;
		}

		if (t0 >= 0 || t1 >= 0) {
			// set isHit to true
			shadeRec.isHit = true;
			if (t0 < 0) {
				t0 = t1;
			}
			if (t0 < shadeRec.tMin) {
				shadeRec.tMin = t0;
				// calculate the hitpoint
				shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(t0));
				// calculate the normal of the hitpoint
				Vector3d untransformedNormal = transformed.origin.add(transformed.direction.scale(t0)).toVector();
				double[] transformedNormal = transformation.getInverseTransformationMatrix().transpose()
						.multiply(untransformedNormal.toQuaternion());
				shadeRec.normal = new Vector3d(transformedNormal[0], transformedNormal[1], transformedNormal[2]);
				shadeRec.objectHit = this;
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean shadowIntersect(Ray ray, Point lightPosition) {
		Ray transformed = transformation.transformInverse(ray);

		Vector3d o = transformed.origin.toVector();

		double a = transformed.direction.dot(transformed.direction);
		double b = 2.0 * (transformed.direction.dot(o));
		double c = o.dot(o) - 1.0;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return false;
		double dr = Math.sqrt(d);

		// numerically solve the equation a*t^2 + b * t + c = 0
		double q = -0.5 * (b < 0 ? (b - dr) : (b + dr));

		double t0 = q / a;
		double t1 = c / q;

		if (t1 < t0) {
			double temp = t1;
			t1 = t0;
			t0 = temp;
		}

		if (t0 >= 1e-6 || t1 >= 1e-6) {
			if (t0 < 0) {
				t0 = t1;
			}
			double length1 = ray.origin.add(ray.direction.scale(t0)).toVector().length();
			double length2 = lightPosition.subtract(ray.origin).length();
			if(length1 < length2)
				return true;
		}
		return false;
	}
}
