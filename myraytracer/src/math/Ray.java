package math;

/**
 * Represents an {@link Ray} in three dimensional space starting at a given
 * {@link Point3d} and extending infinitely in a given direction.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Ray implements Cloneable {
	/**
	 * The origin of the ray.
	 */
	public final Point3d origin;

	/**
	 * The direction the ray is pointing to.
	 */
	public final Vector3d direction;
	
	public final double minT;
	public final double maxT;

	/**
	 * Creates a new {@link Ray} starting at the given origin and propagating
	 * in the given direction.
	 * 
	 * @param origin
	 *            the origin of the ray.
	 * @param direction
	 *            the direction of the ray.
	 * @throws NullPointerException
	 *             when the given origin and/or direction is null.
	 */
	public Ray(Point3d origin, Vector3d direction) throws NullPointerException {
		this(origin, direction, 0, Double.MAX_VALUE);
	}
	
	public Ray(Point3d origin, Vector3d direction, double minT, double maxT){
		if (origin == null)
			throw new NullPointerException("the given origin is null!");
		if (direction == null)
			throw new NullPointerException("the given direction is null!");
		this.origin = origin;
		this.direction = direction;
		this.minT = minT;
		this.maxT = maxT;
	}

	/**
	 * Creates a copy of the given {@link Ray}.
	 * 
	 * @param ray
	 *            the ray to copy.
	 * @throws NullPointerException
	 *             when the given ray is null.
	 */
	public Ray(Ray ray) throws NullPointerException {
		this(ray.origin, ray.direction, ray.minT, ray.maxT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Ray(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[Ray3D] from %s %s %s in direction %s %s %s",
				origin.x, origin.y, origin.z, direction.x, direction.y,
				direction.z);
	}
}
