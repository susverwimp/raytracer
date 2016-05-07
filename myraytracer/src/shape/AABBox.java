package shape;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class AABBox extends GeometricObject {

	public Point3d minPoint;
	public Point3d maxPoint;

	public AABBox(Material material, Point3d minPoint, Point3d maxPoint) {
		this.material = material;
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;

		double a = 1.0 / ray.direction.x;
		if (a >= 0) {
			txMin = (minPoint.x - ray.origin.x) * a;
			txMax = (maxPoint.x - ray.origin.x) * a;
		} else {
			txMin = (maxPoint.x - ray.origin.x) * a;
			txMax = (minPoint.x - ray.origin.x) * a;
		}

		double b = 1.0 / ray.direction.y;
		if (b >= 0) {
			tyMin = (minPoint.y - ray.origin.y) * b;
			tyMax = (maxPoint.y - ray.origin.y) * b;
		} else {
			tyMin = (maxPoint.y - ray.origin.y) * b;
			tyMax = (minPoint.y - ray.origin.y) * b;
		}

		double c = 1.0 / ray.direction.z;
		if (c >= 0) {
			tzMin = (minPoint.z - ray.origin.z) * c;
			tzMax = (maxPoint.z - ray.origin.z) * c;
		} else {
			tzMin = (maxPoint.z - ray.origin.z) * c;
			tzMax = (minPoint.z - ray.origin.z) * c;
		}

		double t0, t1;

		int faceIn, faceOut;
		// find largest entering t value
		if (txMin > tyMin) {
			t0 = txMin;
			faceIn = (a >= 0.0) ? 0 : 3;
		} else {
			t0 = tyMin;
			faceIn = (b >= 0.0) ? 1 : 4;
		}
		if (tzMin > t0) {
			t0 = tzMin;
			faceIn = (c >= 0.0) ? 2 : 5;
		}

		// find smallest exiting t value
		if (txMax < tyMax) {
			t1 = txMax;
			faceOut = (a >= 0.0) ? 3 : 0;
		} else {
			t1 = tyMax;
			faceOut = (b >= 0.0) ? 4 : 1;
		}
		if (tzMax < t1) {
			t1 = tzMax;
			faceOut = (c >= 0.0) ? 5 : 2;
		}

		// condition for hit
		if (t0 < t1 && t1 > kEpsilon) {
			if (t0 > kEpsilon) {
				shadeRec.t = t0;
				shadeRec.normal = getNormal(faceIn);
			} else {
				shadeRec.t = t1;
				shadeRec.normal = getNormal(faceOut);
			}
			shadeRec.localHitPoint = ray.origin.add(ray.direction.scale(shadeRec.t));
			shadeRec.totalIntersections++;
			return true;
		}
		return false;
	}

	public Vector3d getNormal(int faceHit) {
		switch (faceHit) {
		case 0:
			return new Vector3d(-1, 0, 0);
		case 1:
			return new Vector3d(0, -1, 0);
		case 2:
			return new Vector3d(0, 0, -1);
		case 3:
			return new Vector3d(1, 0, 0);
		case 4:
			return new Vector3d(0, 1, 0);
		case 5:
			return new Vector3d(0, 0, 1);
		default:
			return null;
		}
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		if(!shadows)
			return false;
		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;

		double a = 1.0 / shadowRay.direction.x;
		if (a >= 0) {
			txMin = (minPoint.x - shadowRay.origin.x) * a;
			txMax = (maxPoint.x - shadowRay.origin.x) * a;
		} else {
			txMin = (maxPoint.x - shadowRay.origin.x) * a;
			txMax = (minPoint.x - shadowRay.origin.x) * a;
		}

		double b = 1.0 / shadowRay.direction.y;
		if (b >= 0) {
			tyMin = (minPoint.y - shadowRay.origin.y) * b;
			tyMax = (maxPoint.y - shadowRay.origin.y) * b;
		} else {
			tyMin = (maxPoint.y - shadowRay.origin.y) * b;
			tyMax = (minPoint.y - shadowRay.origin.y) * b;
		}

		double c = 1.0 / shadowRay.direction.z;
		if (c >= 0) {
			tzMin = (minPoint.z - shadowRay.origin.z) * c;
			tzMax = (maxPoint.z - shadowRay.origin.z) * c;
		} else {
			tzMin = (maxPoint.z - shadowRay.origin.z) * c;
			tzMax = (minPoint.z - shadowRay.origin.z) * c;
		}

		double t0, t1;

		// find largest entering t value
		if (txMin > tyMin) {
			t0 = txMin;
		} else {
			t0 = tyMin;
		}
		if (tzMin > t0) {
			t0 = tzMin;
		}

		// find smallest exiting t value
		if (txMax < tyMax) {
			t1 = txMax;
		} else {
			t1 = tyMax;
		}
		if (tzMax < t1) {
			t1 = tzMax;
		}

		// condition for hit
		if (t0 < t1 && t1 > kEpsilon) {
			return true;
		}
		return false;
	}

}
