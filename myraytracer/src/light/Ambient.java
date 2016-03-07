package light;

import math.RGBColor;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Ambient extends Light {

	private double ls;
	private RGBColor color;
	
	public Ambient(){
		this.ls = 1.0;
		this.color = new RGBColor(1,1,1);
	}
	
	@Override
	public Vector3d getDirection(ShadeRec shadeRec) {
		return new Vector3d();
	}

	@Override
	public RGBColor L(ShadeRec shadeRec) {
		return color.scale(ls);
	}

	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec shadeRec) {
		return false;
	}

}
