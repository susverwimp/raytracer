package shape.trianglemesh;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import math.Vector3i;
import shape.BBox;
import shape.GeometricObject;

public abstract class MeshTriangle extends GeometricObject {

	public Mesh mesh;
	public Vector3i index0;
	public Vector3i index1;
	public Vector3i index2;
	public Vector3d normal;
	public Point3d centroid;

	public MeshTriangle(Mesh mesh, Vector3i index0, Vector3i index1, Vector3i index2, Material material) {
		this.material = material;
		this.mesh = mesh;
		this.index0 = index0;
		this.index1 = index1;
		this.index2 = index2;
		calculateCentroid();
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		Point3d v0 = (mesh.vertices[index0.x]);
		Point3d v1 = (mesh.vertices[index1.x]);
		Point3d v2 = (mesh.vertices[index2.x]);

		double a = v0.x - v1.x;
		double b = v0.x - v2.x;
		double c = shadowRay.direction.x;
		double d = v0.x - shadowRay.origin.x;
		double e = v0.y - v1.y;
		double f = v0.y - v2.y;
		double g = shadowRay.direction.y;
		double h = v0.y - shadowRay.origin.y;
		double i = v0.z - v1.z;
		double j = v0.z - v2.z;
		double k = shadowRay.direction.z;
		double l = v0.z - shadowRay.origin.z;

		double m = f * k - g * j;
		double n = h * k - g * l;
		double p = f * l - h * j;
		double q = g * i - e * k;
		double s = e * j - f * i;

		double inv_denom = 1.0 / (a * m + b * q + c * s);

		double e1 = d * m - b * n - c * p;
		double beta = e1 * inv_denom;

		if (beta < 0.0)
			return false;

		double r = e * l - h * i;
		double e2 = a * n + d * q + c * r;
		double gamma = e2 * inv_denom;

		if (gamma < 0.0)
			return false;

		if (beta + gamma > 1.0)
			return false;

		double e3 = a * p - b * r + d * s;
		double t = e3 * inv_denom;

		if (t < kEpsilon || t >= distance)
			return false;

		return (true);
	}

	public void computeNormal(boolean reverseNormal) {
		normal = mesh.vertices[index1.x].subtract(mesh.vertices[index0.x])
				.cross(mesh.vertices[index2.x].subtract(mesh.vertices[index0.x])).normalize();
		if (reverseNormal)
			normal = normal.scale(-1);
	}

	public BBox getBoundingBox() {
		double delta = 0.0001;

		Point3d v1 = (mesh.vertices[index0.x]);
		Point3d v2 = (mesh.vertices[index1.x]);
		Point3d v3 = (mesh.vertices[index2.x]);

		BBox b = new BBox(Math.min(Math.min(v1.x, v2.x), v3.x) - delta, Math.min(Math.min(v1.y, v2.y), v3.y) - delta,
				Math.max(Math.max(v1.z, v2.z), v3.z) + delta, Math.max(Math.max(v1.x, v2.x), v3.x) + delta,
				Math.max(Math.max(v1.y, v2.y), v3.y) + delta, Math.min(Math.min(v1.z, v2.z), v3.z) - delta);
		return b;
	}

	public double interpolateU(double beta, double gamma) {
		return (1 - beta - gamma) * mesh.u[index0.y] + beta * mesh.u[index1.y] + gamma * mesh.u[index2.y];
	}

	public double interpolateV(double beta, double gamma) {
		return (1 - beta - gamma) * mesh.v[index0.y] + beta * mesh.v[index1.y] + gamma * mesh.v[index2.y];
	}
	
	private void calculateCentroid(){
		double xSum = (mesh.vertices[index0.x].x + mesh.vertices[index1.x].x + mesh.vertices[index2.x].x)/3;
		double ySum = (mesh.vertices[index0.x].y + mesh.vertices[index1.x].y + mesh.vertices[index2.x].y)/3;
		double zSum = (mesh.vertices[index0.x].z + mesh.vertices[index1.x].z + mesh.vertices[index2.x].z)/3;
		centroid = new Point3d(xSum, ySum, zSum);
		centroid.scale(1/3);
	}
	
	@Override
	public Point3d getCenter(){
		return centroid;
	}
}
