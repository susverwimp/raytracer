package shape;

import math.Ray;
import math.ShadeRec;

public abstract class GeometricObject {
	
	public abstract boolean intersect(Ray ray, ShadeRec shadeRec);

}
