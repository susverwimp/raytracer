package mapping;

import math.Point3d;
import math.Vector2i;

public abstract class Mapping {
	
	public abstract void getTexelCoords(Point3d localHitPoint, int vRes, int hRes, Vector2i pixelCoords);

}
