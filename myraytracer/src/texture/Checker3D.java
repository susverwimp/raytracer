package texture;

import math.RGBColor;
import util.ShadeRec;

public class Checker3D extends Texture {

	private double size;
	private RGBColor color1;
	private RGBColor color2;
	
	public Checker3D(double size, RGBColor color1, RGBColor color2) {
		this.size = size;
		this.color1 = color1;
		this.color2 = color2;
	}

	@Override
	public RGBColor getColor(ShadeRec shadeRec) {
		double eps = -0.000187453738f;
		double x = shadeRec.localHitPoint.x + eps;
		double y = shadeRec.localHitPoint.y + eps;
		double z = shadeRec.localHitPoint.z + eps;
		
		if(((int)Math.floor(x/size) + (int)Math.floor(y / size) + (int)Math.floor(z / size)) % 2 == 0)
			return color1;
		
		return color2;
	}
	
	

}
