package math;

/**
 * Represents a {@link Transformation} to transform three dimensional objects.
 * 
 * @author Niels Billen
 * @version 0.2
 */
public class Transformation implements Cloneable {
	private final Matrix matrix;
	private final Matrix inverse;

	/**
	 * Reference to the identity {@link Transformation}.
	 */
	public static final Transformation IDENTITY = new Transformation(
			Matrix.IDENTITY, Matrix.IDENTITY);

	/**
	 * Creates a new {@link Transformation} for three dimensional objects.
	 * 
	 * @param matrix
	 *            the matrix transformation.
	 * @param inverse
	 *            the inverse of the given transformation.
	 */
	private Transformation(Matrix matrix, Matrix inverse) {
		this.matrix = matrix;
		this.inverse = inverse;
	}

	/**
	 * Creates a copy of the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the {@link Transformation} to copy.
	 * @throws NullPointerException
	 *             when the given {@link Transformation} is null.
	 */
	public Transformation(Transformation transformation)
			throws NullPointerException {
		this.matrix = transformation.matrix;
		this.inverse = transformation.inverse;
	}

	/**
	 * Returns the {@link Matrix} containing the transformation.
	 * 
	 * @return the {@link Matrix} containing the transformation.
	 */
	public Matrix getTransformationMatrix() {
		return matrix;
	}

	/**
	 * Returns the inverse of the {@link Matrix} containing the transformation.
	 * 
	 * @return the inverse of the {@link Matrix} containing the transformation.
	 */
	public Matrix getInverseTransformationMatrix() {
		return inverse;
	}

	/**
	 * Returns the inverse of this {@link Transformation}.
	 * 
	 * @return the inverse of this {@link Transformation}.
	 */
	public Transformation invert() {
		return new Transformation(inverse, matrix);
	}

	/**
	 * Appends the given {@link Transformation} to this {@link Transformation}.
	 * 
	 * @param transformation
	 *            the {@link Transformation} to append.
	 * @throws NullPointerException
	 *             when the given {@link Transformation} is null.
	 * @return this {@link Transformation} concatenated with the given
	 *         {@link Transformation}.
	 */
	public Transformation append(Transformation transformation)
			throws NullPointerException {
		return new Transformation(matrix.multiply(transformation.matrix),
				transformation.inverse.multiply(inverse));
	}

	/**
	 * Transforms the given {@link Point3d} with this {@link Transformation}.
	 * 
	 * @param point
	 *            the {@link Point3d} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Point3d} is null.
	 * @return the given {@link Point3d} transformed by this
	 *         {@link Transformation}.
	 */
	public Point3d transform(Point3d point) throws NullPointerException {
		return matrix.transform(point);
	}

	/**
	 * Transforms the given {@link Point3d} with the inverse of this
	 * {@link Transformation}.
	 * 
	 * @param point
	 *            the {@link Point3d} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Point3d} is null.
	 * @return the given {@link Point3d} transformed by the inverse of this
	 *         {@link Transformation}.
	 */
	public Point3d transformInverse(Point3d point) throws NullPointerException {
		return inverse.transform(point);
	}

	/**
	 * Transforms the given {@link Vector3d} with this {@link Transformation}.
	 * 
	 * @param vector
	 *            the {@link Vector3d} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Vector3d} is null.
	 * @return the given {@link Vector3d} transformed by this
	 *         {@link Transformation}.
	 */
	public Vector3d transform(Vector3d vector) throws NullPointerException {
		return matrix.transform(vector);
	}

	/**
	 * Transforms the given {@link Vector3d} with the inverse of this
	 * {@link Transformation}.
	 * 
	 * @param vector
	 *            the {@link Vector3d} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Vector3d} is null.
	 * @return the given {@link Vector3d} transformed by the inverse of this
	 *         {@link Transformation}.
	 */
	public Vector3d transformInverse(Vector3d vector) throws NullPointerException {
		return inverse.transform(vector);
	}

	/**
	 * Transforms the given {@link Ray} with this {@link Transformation}.
	 * 
	 * @param ray
	 *            the {@link Ray} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Ray} is null.
	 * @return the given {@link Ray} transformed by this {@link Transformation}.
	 */
	public Ray transform(Ray ray) throws NullPointerException {
		Point3d point = transform(ray.origin);
		Vector3d direction = transform(ray.direction);
		return new Ray(point, direction);
	}

	/**
	 * Transforms the given {@link Ray} with the inverse of this
	 * {@link Transformation}.
	 * 
	 * @param ray
	 *            the {@link Ray} to transform.
	 * @throws NullPointerException
	 *             when the given {@link Ray} is null.
	 * @return the given {@link Ray} transformed by the inverse of this
	 *         {@link Transformation}.
	 */
	public Ray transformInverse(Ray ray) throws NullPointerException {
		Point3d point = transformInverse(ray.origin);
		Vector3d direction = transformInverse(ray.direction);
		return new Ray(point, direction);
	}

	/**
	 * Returns the identity {@link Transformation}.
	 * 
	 * @return the identity {@link Transformation}.
	 */
	public static Transformation createIdentity() {
		return IDENTITY;
	}

	/**
	 * Creates a new translation {@link Transformation}.
	 * 
	 * @param x
	 *            the x translation for this {@link Transformation}.
	 * @param y
	 *            the y translation for this {@link Transformation}.
	 * @param z
	 *            the z translation for this {@link Transformation}.
	 * @return a new translation {@link Transformation}.
	 */
	public static Transformation translate(double x, double y, double z) {
		// @formatter:off
		Matrix transformation = new Matrix(	1,	0,	0,	x,
											0,	1,	0,	y,
											0,	0,	1,	z,
											0,	0,	0,	1);
		Matrix inverse = new Matrix(1,	0,	0,	-x,
									0,	1,	0,	-y,
									0,	0,	1,	-z,
									0,	0,	0,	1);
		// @formatter:on
		return new Transformation(transformation, inverse);
	}

	/**
	 * Creates a new scale {@link Transformation}.
	 * 
	 * @param x
	 *            the x scale for this scale {@link Transformation}.
	 * @param y
	 *            the y scale for this scale {@link Transformation}.
	 * @param z
	 *            the z scale for this scale {@link Transformation}.
	 * @return a new {@link Transformation} which scales three dimensional
	 *         objects.
	 */
	public static Transformation scale(double x, double y, double z) {
		// @formatter:off
		Matrix transformation = new Matrix(	x,	0,	0,	0,
											0,	y,	0,	0,
											0,	0,	z,	0,
											0,	0,	0,	1);
		Matrix inverse = new Matrix(1/x,	0,		0,		0,
									0,		1/y,	0,		0,
									0,		0,		1/z,	0,
									0,		0,		0,		1);
		// @formatter:on
		return new Transformation(transformation, inverse);
	}

	/**
	 * Creates a new rotation {@link Transformation} about the x axis in a
	 * counter clockwise direction.
	 * 
	 * @param angle
	 *            the angle to rotate about (in degrees).
	 * @return a new rotation {@link Transformation} about the x axis.
	 */
	public static Transformation rotateX(double angle) {
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);

		// @formatter:off
		Matrix transformation = new Matrix(	1,		0,		0,		0,
											0,		cos,	-sin,	0,
											0,		sin,	cos,	0,
											0,		0,		0,		1);
		Matrix inverse = transformation.transpose();
		// @formatter:on
		return new Transformation(transformation, inverse);
	}

	/**
	 * Creates a new rotation {@link Transformation} about the y axis in a
	 * counter clockwise direction.
	 * 
	 * @param angle
	 *            the angle to rotate about (in degrees).
	 * @return a new rotation {@link Transformation} about the y axis.
	 */
	public static Transformation rotateY(double angle) {
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);

		// @formatter:off
		Matrix transformation = new Matrix(	cos,	0,		sin,	0,
											0,		1,		0,		0,
											-sin,	0,		cos,	0,
											0,		0,		0,		1);
		Matrix inverse = transformation.transpose();
		// @formatter:on
		return new Transformation(transformation, inverse);
	}

	/**
	 * Creates a new rotation {@link Transformation} about the z axis in a
	 * counter clockwise direction.
	 * 
	 * @param angle
	 *            the angle to rotate about (in degrees).
	 * @return a new rotation {@link Transformation} about the z axis.
	 */
	public static Transformation rotateZ(double angle) {
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);

		// @formatter:off
		Matrix transformation = new Matrix(	cos,	-sin,	0,		0,
											sin,	cos,	0,		0,
											0,		0,		1,		0,
											0,		0,		0,		1);
		Matrix inverse = transformation.transpose();
		// @formatter:on
		return new Transformation(transformation, inverse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Transformation(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("[").append(getClass().getName()+"]\n").append(
				matrix.toString()).toString();
	}
}
