package math;

import shape.GeometricObject;

public class ShadeRec {
	
	public static final double tMax = Double.POSITIVE_INFINITY;
	
	public boolean isHit;
	public double tMin = tMax;
	public Point localHitPoint;
	public Vector3d normal;
	public GeometricObject objectHit;
	public RGBColor color = new RGBColor(0, 0, 0);

}
