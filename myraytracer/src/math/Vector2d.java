package math;

public class Vector2d {
	
	/**
	 * x coordinate of this {@link Vector3d}.
	 */
	public final double x;

	/**
	 * y coordinate of this {@link Vector3d}.
	 */
	public final double y;
	
	public Vector2d() {
		this(0, 0);
	}

	/**
	 * Creates a new {@link Vector3d} at the given position.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 */
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}


}
