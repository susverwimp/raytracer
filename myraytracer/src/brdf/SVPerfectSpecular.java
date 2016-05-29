package brdf;

import math.RGBColor;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVPerfectSpecular extends BRDF {

	public double kr;
	public Texture cr;
	
	public SVPerfectSpecular(Texture cr) {
		this.cr = cr;
	}
	
	@Override
	public RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo) {
		double nDotWo = shadeRec.normal.dot(wo);
		Vector3d v = shadeRec.normal.scale(2 * nDotWo);
		wi.set(wo.scale(-1).add(v));
		shadeRec.pdf = Math.abs(shadeRec.normal.dot(wi));
		
		return cr.getColor(shadeRec).scale(kr);
	}

}
