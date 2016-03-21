package shape;

import java.util.Collections;
import java.util.Comparator;

import math.Point3d;
import math.Ray;
import math.Vector2d;
import math.Vector3d;
import shape.trianglemesh.MeshTriangle;
import util.ShadeRec;

public class BoundingVolume extends Compound {

	public BBox boundingBox;

	public void calculateHierarchy() {
		if (objects.size() > 1) {
			Point3d p0 = getMinCoordinates();
			Point3d p1 = getMaxCoordinates();
			boundingBox = new BBox(p0, p1);

			double xDistance = Math.abs(boundingBox.maxPoint.x - boundingBox.minPoint.x);
			double yDistance = Math.abs(boundingBox.maxPoint.y - boundingBox.minPoint.y);
			double zDistance = Math.abs(boundingBox.maxPoint.z - boundingBox.minPoint.z);

			int sortAxis = 0;
			if (yDistance >= xDistance && yDistance >= zDistance)
				sortAxis = 1;
			if (zDistance >= xDistance && zDistance >= yDistance)
				sortAxis = 2;

			Comparator<GeometricObject> comparator = null;
			switch (sortAxis) {
			case 0:
				comparator = GeometricObject.xAxisComparator;
				break;
			case 1:
				comparator = GeometricObject.yAxisComparator;
				break;
			case 2:
				comparator = GeometricObject.zAxisComparator;
				break;
			}

			Collections.sort(objects, comparator);
			BoundingVolume left = new BoundingVolume();
			for (int i = 0; i < objects.size() / 2; i++) {
				left.addObject(objects.get(i));
			}
			left.calculateHierarchy();
			BoundingVolume right = new BoundingVolume();
			for (int i = objects.size() / 2; i < objects.size(); i++) {
				right.addObject(objects.get(i));
			}
			right.calculateHierarchy();
			objects.clear();
			addObject(left);
			addObject(right);
		} else {
			boundingBox = objects.get(0).getBoundingBox();
		}

	}

	public Point3d getMinCoordinates() {
		BBox bbox;
		Point3d p0 = new Point3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

		for (GeometricObject object : objects) {
			bbox = object.getBoundingBox();
			if (bbox.minPoint.x < p0.x)
				p0.x = bbox.minPoint.x;
			if (bbox.minPoint.y < p0.y)
				p0.y = bbox.minPoint.y;
			if (bbox.minPoint.z > p0.z)
				p0.z = bbox.minPoint.z;
		}

		return p0;
	}

	public Point3d getMaxCoordinates() {
		BBox bbox;
		Point3d p0 = new Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		for (GeometricObject object : objects) {
			bbox = object.getBoundingBox();
			if (bbox.maxPoint.x > p0.x)
				p0.x = bbox.maxPoint.x;
			if (bbox.maxPoint.y > p0.y)
				p0.y = bbox.maxPoint.y;
			if (bbox.maxPoint.z < p0.z)
				p0.z = bbox.maxPoint.z;
		}

		return p0;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		if (boundingBox.intersect(ray, shadeRec)) {
			Vector3d normal = new Vector3d();
			Point3d localHitPoint = new Point3d();
			Vector2d textureCoords = new Vector2d();
			double tmin = Double.POSITIVE_INFINITY;
			for (GeometricObject object : objects) {
				if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
					shadeRec.isHit = true;
					tmin = shadeRec.t;
					material = object.material;
					normal.set(shadeRec.normal.x, shadeRec.normal.y, shadeRec.normal.z);
					localHitPoint.set(shadeRec.localHitPoint.x, shadeRec.localHitPoint.y, shadeRec.localHitPoint.z);
					textureCoords.setX(shadeRec.textureCoords.x);
					textureCoords.setY(shadeRec.textureCoords.y);
				}
			}
			if (shadeRec.isHit) {
				shadeRec.t = tmin;
				shadeRec.normal = normal;
				shadeRec.localHitPoint = localHitPoint;
				shadeRec.textureCoords = textureCoords;
			}

			return shadeRec.isHit;
		}
		return false;
	}
	
	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		if(boundingBox.shadowHit(shadowRay, distance)){
			for(GeometricObject object : objects){
				if(object.shadowHit(shadowRay, distance)){
					return true;
				}
			}
		}
		return false;
	}
}
