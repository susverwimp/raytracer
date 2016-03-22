package math;

public class Vector2d {
	
	/**
	 * x coordinate of this {@link Vector3d}.
	 */
	public double x;

	/**
	 * y coordinate of this {@link Vector3d}.
	 */
	public double y;
	
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString(){
		return "x: " + x + " y: " + y;
	}
}
