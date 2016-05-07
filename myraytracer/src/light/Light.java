package light;

import math.RGBColor;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public abstract class Light {

	public boolean castShadows = true;
	
	public abstract Vector3d getDirection(ShadeRec shadeRec);
	public abstract RGBColor L(ShadeRec shadeRec);
	
	public abstract boolean inShadow(Ray shadowRay, ShadeRec shadeRec);
	public void setShadows(boolean castShadows){
		this.castShadows = castShadows;
	}
	public double G(ShadeRec shadeRec){
		return 1.0;
	}
	public double pdf(ShadeRec shadeRec){
		return 1.0;
	}
}
