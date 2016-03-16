package texture;

import math.RGBColor;
import util.ShadeRec;

public class ConstantColor extends Texture {
	
	RGBColor color;
	
	public ConstantColor(RGBColor color){
		this.color = color;
	}

	@Override
	public RGBColor getColor(ShadeRec shadeRec) {
		return color;
	}
	
	

}
