package brdf;

import math.RGBColor;
import math.Vector3d;
import util.ShadeRec;

public class Lambertian extends BRDF {
	
	public double kd;
	public RGBColor cd;
	
	public Lambertian(){
		this(0.5, new RGBColor(0.5, 0.5, 0.5));
	}
	
	public Lambertian(double kd, RGBColor color){
		this.kd = kd;
		this.cd = color;
	}

	@Override
	public RGBColor f(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		return cd.scale(kd/Math.PI);
	}

	@Override
	public RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		return null;
	}

	@Override
	public RGBColor rho(ShadeRec shadeRec, Vector3d wo) {
		return cd.scale(kd);
	}
	
	
	
}
