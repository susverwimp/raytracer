package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;

public class Model extends GeometricObject {

	
	
	public Model(RGBColor color) {
		super(color);
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec shadeRec) {
		return false;
	}

	@Override
	public boolean shadowIntersect(Ray ray, Point lightPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
