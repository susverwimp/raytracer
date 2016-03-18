package brdf;

import math.RGBColor;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVGlossySpecular extends BRDF {

	public double ks;
	public double exp;
	public Texture cd;
	
	public SVGlossySpecular(Texture cd) {
		this.cd = cd;
	}
	
	@Override
	public RGBColor f(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		RGBColor L = new RGBColor();
		double nDotWi = shadeRec.normal.dot(wi);
		Vector3d r = wi.scale(-1).add(shadeRec.normal.scale(2 * nDotWi));
		double rDotWi = r.dot(wi);
		if(rDotWi > 0)
			L = cd.getColor(shadeRec).scale(ks * Math.pow(rDotWi, exp));
		return L;
	}

	@Override
	public RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		return null;
	}

	@Override
	public RGBColor rho(ShadeRec shadeRec, Vector3d wo) {
		return new RGBColor();
	}

}
