package light;

import math.RGBColor;

public abstract class Light {
	
	public RGBColor color;
	public double intensity;
	
	public Light(RGBColor color, double intensity){
		this.color = color;
		this.intensity = intensity;
	}

}
