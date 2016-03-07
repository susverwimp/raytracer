package material;

import math.RGBColor;
import util.ShadeRec;

public abstract class Material {
	
	public abstract RGBColor shade(ShadeRec shadeRec);

}
