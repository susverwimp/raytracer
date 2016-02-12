package math;

public class ShadeRec {
	
	public static final double tMax = 10000;
	
	public boolean isHit;
	public double tMin = tMax;
	public Point localHitPoint;
	public Vector normal;
	public RGBColor color = new RGBColor(0, 0, 0);

}
