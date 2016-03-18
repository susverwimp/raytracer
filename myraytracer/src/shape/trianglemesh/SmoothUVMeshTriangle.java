package shape.trianglemesh;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector3i;
import util.ShadeRec;

public class SmoothUVMeshTriangle extends SmoothMeshTriangle {

	public SmoothUVMeshTriangle(Mesh mesh, Vector3i index0, Vector3i index1, Vector3i index2,
			Material material) {
		super(mesh, index0, index1, index2, material);
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		Point3d v0 = mesh.vertices[index0.x];
		Point3d v1 = mesh.vertices[index1.x];
		Point3d v2 = mesh.vertices[index2.x];
	
		double a = v0.x - v1.x;
		double b = v0.x - v2.x;
		double c = ray.direction.x;
		double d = v0.x - ray.origin.x;
		double e = v0.y - v1.y;
		double f = v0.y - v2.y;
		double g = ray.direction.y;
		double h = v0.y - ray.origin.y;
		double i = v0.z - v1.z;
		double j = v0.z - v2.z;
		double k = ray.direction.z;
		double l = v0.z - ray.origin.z;
	
		double m = f * k - g * j; 
		double n = h * k - g * l; 
		double p = f * l - h * j;
		double q = g * i - e * k; 
		double s = e * j - f * i;
	
	    double inv_denom  = 1.0 / (a * m + b * q + c * s);
	
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
	
	    if (t < kEpsilon)
	        return false;
	
	    shadeRec.t = t;
	    shadeRec.normal = interpolateNormal(beta, gamma);
	    shadeRec.localHitPoint 	= ray.origin.add(ray.direction.scale(t));
	    shadeRec.textureCoords.x = interpolateU(beta, gamma);
	    shadeRec.textureCoords.y = interpolateV(beta, gamma);
	    shadeRec.totalIntersections++;
	    return true;
	}
}
