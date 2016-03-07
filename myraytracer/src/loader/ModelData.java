package loader;

import math.Point3d;
import math.Vector3d;
import shape.trianglemesh.Mesh;

public class ModelData {
	private double[] textureCoords;
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
		mesh = new Mesh(verticesInPoints, normalsInVectors);
		this.textureCoords = textureCoords;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public double[] getTextureCoords() {
		return textureCoords;
	}

	public int[] getIndices() {
		return indices;
	}

	public double getFurthestPoint() {
		return furthestPoint;
	}
}
