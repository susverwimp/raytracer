package math;

import java.util.Comparator;
import java.util.Locale;

/**
 * Point implementation in three dimensions.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Point3d implements Cloneable, Comparable<Point3d> {
	/**
	 * x coordinate of this {@link Point3d}.
	 */
	public double x;

	/**
	 * y coordinate of this {@link Point3d}.
	 */
	public double y;

	/**
	 * z coordinate of this {@link Point3d}.
	 */
	public double z;

	/**
	 * Creates a {@link Point3d} at the origin.
	 */
	public Point3d() {
		this(0, 0, 0);
	}

	/**
	 * Creates a new {@link Point3d} at the given position.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 */
	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new {@link Point3d} at the given position, scaled by the given
	 * homogeneous coordinate.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @param w
	 *            the homogeneous coordinate.
	 */
	public Point3d(double x, double y, double z, double w) {
		double inv_w = 1.0 / w;
		this.x = x * inv_w;
		this.y = y * inv_w;
		this.z = z * inv_w;
	}

	/**
	 * Creates a copy of the given {@link Point3d}.
	 * 
	 * @param point
	 *            the {@link Point3d} to copy.
	 * @throws NullPointerException
	 *             when the given {@link Point3d} is null.
	 */
	public Point3d(Point3d point) throws NullPointerException {
		this(point.x, point.y, point.z);
	}

	/**
	 * Returns the coordinate of this {@link Point3d} along the given axis.
	 * 
	 * @param axis
	 *            axis to retrieve the coordinate of (0=x, 1=y, 2=z axis).
	 * @throws IllegalArgumentException
	 *             when the given axis is smaller than zero or larger than two.
	 * @return the coordinate of this {@link Point3d} along the given axis.
	 */
	public double get(int axis) throws IllegalArgumentException {
		switch (axis) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		default:
			throw new IllegalArgumentException(
					"the given axis is out of bounds!");
		}
	}
	
	public void set(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a {@link Point3d} equal to this {@link Point3d} translated by the
	 * given coordinates.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @return a new {@link Point3d} which is equal to this {@link Point3d}
	 *         translated by the given coordinates.
	 */
	public Point3d add(double x, double y, double z) {
		return new Point3d(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * Constructs a {@link Point3d} equal to this {@link Point3d} translated by the
	 * given {@link Vector3d}.
	 * 
	 * @param vector
	 *            the {@link Vector3d} to add to this {@link Point3d}.
	 * @throws NullPointerException
	 *             when the given {@link Vector3d} is null.
	 * @return a new {@link Point3d} which is equal to this {@link Point3d}
	 *         translated by the given {@link Vector3d}.
	 */
	public Point3d add(Vector3d vector) throws NullPointerException {
		return add(vector.x, vector.y, vector.z);
	}

	/**
	 * Constructs the {@link Vector3d} spanned between this {@link Point3d} and the
	 * given coordinates.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @return a new {@link Vector3d} equal to the {@link Vector3d} spanned between
	 *         this {@link Point3d} and the given coordinates.
	 */
	public Vector3d subtract(double x, double y, double z) {
		return new Vector3d(this.x - x, this.y - y, this.z - z);
	}

	/**
	 * Constructs the {@link Vector3d} spanned between this {@link Point3d} and the
	 * given {@link Point3d}.
	 * 
	 * @param point
	 *            the {@link Point3d} to span the {@link Vector3d} with.
	 * @throws NullPointerException
	 *             when the given {@link Point3d} is null.
	 * @return a new {@link Vector3d} equal to the {@link Vector3d} spanned between
	 *         this {@link Point3d} and the given {@link Point3d}.
	 */
	public Vector3d subtract(Point3d point) throws NullPointerException {
		return subtract(point.x, point.y, point.z);
	}

	/**
	 * Constructs the {@link Point3d} equal to this {@link Point3d} with its
	 * coordinates scaled by the given scalar.
	 * 
	 * @param scalar
	 *            the scalar to scale this {@link Point3d} with.
	 * @return a new {@link Point3d} equal to this {@link Point3d} with its
	 *         coordinates scaled by the given scalar.
	 */
	public Point3d scale(double scalar) {
		return new Point3d(x * scalar, y * scalar, z * scalar);
	}

	/**
	 * Constructs the {@link Point3d} equal to this {@link Point3d} with its
	 * coordinates divided by the given divisor.
	 * 
	 * @param divisor
	 *            the divisor to scale this {@link Point3d} with.
	 * @return a new {@link Point3d} equal to this {@link Point3d} with its
	 *         coordinates divided by the given divisor.
	 */
	public Point3d divide(double divisor) {
		return scale(1.0 / divisor);
	}

	/**
	 * Returns a comparator to sort {@link Point3d} objects along the given axis.
	 * 
	 * @param axis
	 *            the axis to sort the {@link Point3d}s along.
	 * @throws IllegalArgumentException
	 *             when the given axis is smaller than zero or larger than two.
	 * @return a comparator to sort objects of {@link Point3d} along the given
	 *         axis.
	 */
	public Comparator<Point3d> getComparator(final int axis)
			throws IllegalArgumentException {
		return new Comparator<Point3d>() {
			@Override
			public int compare(Point3d o1, Point3d o2) {
				if (o1.get(axis) < o2.get(axis))
					return -1;
				else if (o1.get(axis) > o2.get(axis))
					return 1;
				else
					return 0;
			}
		};
	}

	/**
	 * Converts this {@link Point3d} to a three dimensional array.
	 * 
	 * @return this {@link Point3d} as a three dimensional array.
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	/**
	 * Returns this {@link Point3d} as a four dimensional array with the fourth
	 * coordinate being the homogeneous coordinate.
	 * 
	 * @return this {@link Point3d} as a four dimensional array with the fourth
	 *         coordinate being the homogeneous coordinate.
	 */
	public double[] toHomogenousArray() {
		return new double[] { x, y, z, 1.0 };
	}

	/**
	 * Converts this {@link Point3d} to a {@link Vector3d}.
	 * 
	 * @return this {@link Point3d} as a {@link Vector3d}.
	 */
	public Vector3d toVector() {
		return new Vector3d(x, y, z);
	}
	
	public double distanceSquared(Point3d p){
		return ((p.x - x) * (p.x - x)) + ((p.y - y) * (p.y - y)) + ((p.z - z) * (p.z - z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Point3d(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Point3d point) {
		if (x < point.x)
			return -1;
		else if (x > point.x)
			return 1;
		else if (y < point.y)
			return -1;
		else if (y > point.y)
			return 1;
		else if (z < point.z)
			return -1;
		else if (z > point.z)
			return 1;
		else
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[%s]:\n%g %g %g", getClass()
				.getName(), x, y, z);
	};
}
