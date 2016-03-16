package loader;

import math.Point3d;
import math.Vector3d;
import shape.trianglemesh.Mesh;

public class ModelData {
	private int[] indices;
	private double furthestPoint;
	public Mesh mesh;

	public ModelData(double[] vertices, double[] textureCoords, double[] normals,
			int[] indices, double furthestPoint) {
		Point3d[] verticesInPoints = new Point3d[vertices.length/3];
		for(int i = 0; i < vertices.length; i+=3){
			verticesInPoints[i/3] = new Point3d(vertices[i], vertices[i+1], vertices[i+2]);
		}
		Vector3d[] normalsInVectors = new Vector3d[normals.length/3];
		for(int i = 0; i < normals.length; i+=3){
			normalsInVectors[i/3] = new Vector3d(normals[i], normals[i+1], normals[i+2]);
		}
		double[] u = new double[textureCoords.length / 2];
		double[] v = new double[textureCoords.length / 2];
		for(int i = 0; i < textureCoords.length; i+= 2){
			u[i/2] = textureCoords[i];
			v[i/2] = textureCoords[i+1];
		}
		mesh = new Mesh(verticesInPoints, normalsInVectors, u, v);
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public int[] getIndices() {
		return indices;
	}

	public double getFurthestPoint() {
		return furthestPoint;
	}
}
