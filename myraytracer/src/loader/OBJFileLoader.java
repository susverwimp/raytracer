package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import math.Point3d;
import math.Vector2d;
import math.Vector3d;
import math.Vector3i;
import shape.trianglemesh.Mesh;

public class OBJFileLoader {
	
	public static Mesh loadOBJ(String filePath) {
		FileReader isr = null;
		File objFile = new File(filePath);
		try {
			isr = new FileReader(objFile);
		} catch (FileNotFoundException e) {
			System.err.println("File not found in res; don't use any extention");
		}
		BufferedReader reader = new BufferedReader(isr);
		String line;
		
		List<Point3d> vertices = new ArrayList<>();
		List<Vector2d> textures = new ArrayList<>();
		List<Vector3d> normals = new ArrayList<>();
		List<Vector3i> indices = new ArrayList<>();
		try {
			while (true) {
				line = reader.readLine();
				if (line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					Point3d vertex = new Point3d(Double.valueOf(currentLine[1]),
							Double.valueOf(currentLine[2]),
							Double.valueOf(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					double u = Math.abs(Double.valueOf(currentLine[1]));
					while(u > 1)
						u -= 1;
					double v = Math.abs(Double.valueOf(currentLine[2]));
					while(v > 1)
						v-=1;
					v = 1 - v;
					Vector2d texture = new Vector2d(u,v);
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3d normal = new Vector3d(Double.valueOf(currentLine[1]),
							Double.valueOf(currentLine[2]),
							Double.valueOf(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					break;
				}
			}
			while (line != null && line.startsWith("f ")) {
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				indices.add(new Vector3i(Integer.valueOf(vertex1[0]) - 1, Integer.valueOf(vertex1[1]) - 1, Integer.valueOf(vertex1[2]) - 1));
				indices.add(new Vector3i(Integer.valueOf(vertex2[0]) - 1, Integer.valueOf(vertex2[1]) - 1, Integer.valueOf(vertex2[2]) - 1));
				indices.add(new Vector3i(Integer.valueOf(vertex3[0]) - 1, Integer.valueOf(vertex3[1]) - 1, Integer.valueOf(vertex3[2]) - 1));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading the file");
		}
		
		Point3d[] verticesArray = new Point3d[vertices.size()];
		Vector3d[] normalsArray = new Vector3d[normals.size()];
		double[] u = new double[textures.size()];
		double[] v = new double[textures.size()];
		Vector3i[] indicesArray = new Vector3i[indices.size()];
		
		for(int i = 0; i < vertices.size(); i++){
			verticesArray[i] = vertices.get(i);
		}
		for(int i = 0; i < normals.size(); i++){
			normalsArray[i] = normals.get(i);
		}
		for(int i = 0; i < textures.size(); i++){
			u[i] = textures.get(i).x;
			v[i] = textures.get(i).y;
		}
		for(int i = 0; i < indices.size(); i++){
			indicesArray[i] = indices.get(i);
		}
		return new Mesh(verticesArray, normalsArray, u, v, indicesArray);
	}

}
