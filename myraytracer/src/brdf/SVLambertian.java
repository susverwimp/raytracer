package brdf;

import math.RGBColor;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVLambertian extends BRDF {

	public double kd;
	public Texture cd;
	
	public SVLambertian(Texture cd) {
		this.cd = cd;
	}
	
	@Override
	public RGBColor f(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		return (cd.getColor(shadeRec).scale(kd / Math.PI));
	}

	@Override
	public RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		return null;
	}

	@Override
	public RGBColor rho(ShadeRec shadeRec, Vector3d wo) {
		return (cd.getColor(shadeRec).scale(kd));
	}

}
