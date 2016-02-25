package loader;

public class ModelData {
	private double[] vertices;
	private double[] textureCoords;
	private double[] normals;
	private int[] indices;
	private double furthestPoint;

	public ModelData(double[] vertices, double[] textureCoords, double[] normals,
			int[] indices, double furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public double[] getVertices() {
		return vertices;
	}

	public double[] getTextureCoords() {
		return textureCoords;
	}

	public double[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public double getFurthestPoint() {
		return furthestPoint;
	}
}
