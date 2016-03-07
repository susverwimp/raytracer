package shape.trianglemesh;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3d;
import shape.GeometricObject;
import util.ShadeRec;

public abstract class MeshTriangle extends GeometricObject {

	public Mesh mesh;
	public int index0;
	public int index1;
	public int index2;
	public Vector3d normal;
	
	public MeshTriangle(Mesh mesh, int index0, int index1, int index2, Material material) {
		super(material);
		this.mesh = mesh;
		this.index0 = index0;
		this.index1 = index1;
		this.index2 = index2;
	}

	@Override
	public boolean shadowHit(Ray shadowRay, double distance) {
		Point3d v0 = (mesh.vertices[index0]);
		Point3d v1 = (mesh.vertices[index1]);
		Point3d v2 = (mesh.vertices[index2]);

	    double a = v0.x - v1.x, b = v0.x - v2.x, c = shadowRay.direction.x, d = v0.x - shadowRay.origin.x;
	    double e = v0.y - v1.y, f = v0.y - v2.y, g = shadowRay.direction.y, h = v0.y - shadowRay.origin.y;
	    double i = v0.z - v1.z, j = v0.z - v2.z, k = shadowRay.direction.z, l = v0.z - shadowRay.origin.z;

	    double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
	    double q = g * i - e * k, s = e * j - f * i;

	    double inv_denom  = 1.0 / (a * m + b * q + c * s);

	    double e1 = d * m - b * n - c * p;
	    double beta = e1 * inv_denom;

	    if (beta < 0.0)
	        return false;

	    double r = e * l - h * i;
	    double e2 = a * n + d * q + c * r;
	    double gamma = e2 * inv_denom;

	    if (gamma < 0.0 )
	        return false;

	    if (beta + gamma > 1.0)
	        return false;

	    double e3 = a * p - b * r + d * s;
	    double t = e3 * inv_denom;

	    if (t < kEpsilon || t >= distance)
	        return false;

	    return (true);
	}

}
