package shape;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;

public abstract class GeometricObject {
	
	public RGBColor color;
	
	public GeometricObject(RGBColor color){
		this.color = color;
	}
	
	public abstract boolean intersect(Ray ray, ShadeRec shadeRec);
	public abstract boolean shadowIntersect(Ray ray, Point lightPosition);

}
