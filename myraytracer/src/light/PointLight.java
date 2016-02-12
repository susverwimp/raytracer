package light;

import math.Point;
import math.RGBColor;

public class PointLight extends Light {

	public Point origin;
	
	public PointLight(RGBColor color, double intensity, Point origin) {
		super(color, intensity);
		this.origin = origin;
	}

}
