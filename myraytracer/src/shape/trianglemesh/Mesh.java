package shape.trianglemesh;

import math.Point3d;
import math.Vector3d;

public class Mesh {
	
	public Point3d[] vertices;
	public Vector3d[] normals;
	public double[] u;
	public double[] v;
	public int numberOfVertices;
	public int numberOfTriangles;
	
	public Mesh(Point3d[] vertices, Vector3d[] normals, double[] u, double[] v){
		this.vertices = vertices;
		this.normals = normals;
		this.u = u;
		this.v = v;
	}
	
}
