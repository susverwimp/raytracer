package math;

public class Quaternion {
	
	/**
	 * x coordinate of this {@link Vector3d}.
	 */
	public final double x;

	/**
	 * y coordinate of this {@link Vector3d}.
	 */
	public final double y;

	/**
	 * z coordinate of this {@link Vector3d}.
	 */
	public final double z;
	
	public final double w;

	/**
	 * Creates a {@link Vector3d} at the origin.
	 */
	public Quaternion() {
		this(0, 0, 0, 0);
	}
	
	public Quaternion(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

}
