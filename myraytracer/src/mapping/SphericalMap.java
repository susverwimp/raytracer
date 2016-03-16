package mapping;

import math.Point3d;
import math.Vector2i;

public class SphericalMap extends Mapping {

	@Override
	public void getTexelCoords(Point3d localHitPoint, int vRes, int hRes, Vector2i pixelCoords) {
		// compute theta and phi
		double theta = Math.acos(localHitPoint.y);
		double phi = Math.atan2(localHitPoint.x, localHitPoint.z);
		if (phi < 0.0)
			phi += Math.PI * 2;

		// map theta and phi to (u, v) in [0, 1] x [0, 1]
		double u = phi / (Math.PI * 2);
		double v = 1 - theta / Math.PI;
		
		// map u and v to the texel coordinates
		pixelCoords.x = (int) ((hRes - 1) * u);
		pixelCoords.y = (int) ((vRes - 1) * v);
	}

}
