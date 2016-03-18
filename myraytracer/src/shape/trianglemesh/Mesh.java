package shape.trianglemesh;

import math.Point3d;
import math.Vector3d;
import math.Vector3i;

public class Mesh {
	
	public Point3d[] vertices;
	public Vector3d[] normals;
	public double[] u;
	public double[] v;
	public Vector3i[] indices;
	
	public Mesh(Point3d[] vertices, Vector3d[] normals, double[] u, double[] v, Vector3i[] indices){
		this.vertices = vertices;
		this.normals = normals;
		this.u = u;
		this.v = v;
		this.indices = indices;
	}

}
