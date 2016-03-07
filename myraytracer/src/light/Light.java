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
	
}
