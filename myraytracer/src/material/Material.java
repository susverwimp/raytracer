package material;

import math.RGBColor;
import util.ShadeRec;

public abstract class Material {
	
	public abstract RGBColor shade(ShadeRec shadeRec);
	public abstract RGBColor areaLightShade(ShadeRec shadeRec);
	public abstract RGBColor pathShade(ShadeRec shadeRec);
	public abstract RGBColor hybridPathShade(ShadeRec shadeRec);
	public RGBColor getLE(ShadeRec shadeRec){
		return new RGBColor();
	}
}
