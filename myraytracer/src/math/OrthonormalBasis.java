package math;

import java.util.Locale;

/**
 * Represents a basis in three dimensions consisting of three orthogonal
 * {@link Vector3d}s of unit length.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class OrthonormalBasis implements Cloneable {
	/**
	 * First vector spanning the orthonormal basis.
	 */
	public final Vector3d u;

	/**
	 * Second vector spanning the orthonormal basis.
	 */
	public final Vector3d v;

	/**
	 * Third vector of spanning the orthonormal basis.
	 */
	public final Vector3d w;

	/**
	 * Creates an orthonormal basis around the given {@link Vector3d}.
	 * 
	 * The <code>w</code> {@link Vector3d} of this {@link OrthonormalBasis} basis
	 * will point in the same direction as the given {@link Vector3d}
	 * <code>a</code>.
	 * 
	 * @param a
	 *            {@link Vector3d} to construct the orthonormal basis about. The
	 *            <code>w</code> {@link Vector3d} of this {@link OrthonormalBasis}
	 *            will point in the direction of this parameter.
	 * @throws NullPointerException
	 *             when the given {@link Vector3d} is null.
	 */
	public OrthonormalBasis(Vector3d a) throws NullPointerException {
		w = a.normalize();

		if (w.x > w.y) {
			double inv_length = 1.0 / Math.sqrt(w.x * w.x + w.z * w.z);
			u = new Vector3d(-w.z * inv_length, 0.0, w.x * inv_length);
		} else {
			double inv_length = 1.0 / Math.sqrt(w.y * w.y + w.z * w.z);
			u = new Vector3d(0.f, w.z * inv_length, -w.y * inv_length);
		}
		v = w.cross(u);
	}

	/**
	 * Creates an orthonormal basis from the two given {@link Vector3d}s.
	 * 
	 * The <code>w</code> {@link Vector3d} of this {@link OrthonormalBasis} basis
	 * will point in the same direction as the given {@link Vector3d}
	 * <code>a</code>. The constructor tries to force the <code>v</code>
	 * {@link Vector3d} of this {@link OrthonormalBasis} to point roughly in the
	 * same direction as the given <code>b</code> {@link Vector3d}.
	 * 
	 * @param a
	 *            the first {@link Vector3d}. The <code>w</code> {@link Vector3d} of
	 *            this {@link OrthonormalBasis} basis will point in the same
	 *            direction as the given {@link Vector3d} <code>a</code>.
	 * @param b
	 *            the second {@link Vector3d}. The <code>v</code> {@link Vector3d}
	 *            will point roughly in the same direction as the given
	 *            {@link Vector3d}.
	 * @throws NullPointerException
	 *             when one of the two vectors is null.
	 */
	public OrthonormalBasis(Vector3d a, Vector3d b) throws NullPointerException {
		if (Math.abs(a.normalize().dot(b.normalize())) > 1.f - 1e-9f) {
			System.err.println("Warning: vector a and b are nearly colinear");
			System.err.println(a);
			System.err.println(b);
		}
		w = a.normalize();
		u = b.cross(w).normalize();
		v = w.cross(u);
	}

	/**
	 * Constructs a copy of the given {@link OrthonormalBasis}.
	 * 
	 * @param basis
	 *            the {@link OrthonormalBasis} to copy.
	 * @throws NullPointerException
	 *             when the given {@link OrthonormalBasis} is null.
	 */
	public OrthonormalBasis(OrthonormalBasis basis) throws NullPointerException {
		this.u = basis.u;
		this.v = basis.v;
		this.w = basis.w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new OrthonormalBasis(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
				"[%s %s %s]\n[%s %s %s]\n[%s %s %s]", u.x, u.y, u.z, v.x, v.y,
				v.z, w.x, w.y, w.z);
	}
}
