package util;

import material.Material;
import math.Point3d;
import math.Ray;
import math.Vector2d;
import math.Vector3d;
import shape.GeometricObject;
import tracer.World;

public class ShadeRec {

	public double t;
	public boolean isHit;
	public Material material;
	public Point3d hitPoint;
	public Point3d localHitPoint;
	public Vector3d normal;
	public Vector2d textureCoords;
	public Ray ray;
	public Vector3d direction;
	public World world;
	public int totalIntersections;
	public GeometricObject object;
	
	public ShadeRec(World world) {
		this.world = world;
		isHit = false;
		hitPoint = new Point3d();
		localHitPoint = new Point3d();
		normal = new Vector3d();
		direction = new Vector3d();
		textureCoords = new Vector2d();
		t = Double.POSITIVE_INFINITY;
	}

}
