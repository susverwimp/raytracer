package brdf;

import math.Point3d;
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
	public RGBColor rho(ShadeRec shadeRec, Vector3d wo) {
		return (cd.getColor(shadeRec).scale(kd));
	}
	
	public RGBColor sampleF(ShadeRec shadeRec, Vector3d wo, Vector3d wi){
		Vector3d w = shadeRec.normal;
		Vector3d v = new Vector3d(0.0034, 1.0, 0.0071).cross(w).normalize();
		Vector3d u = v.cross(w);
		
		Point3d sample = shadeRec.materialSampler.getSampleUnitHemisphere();
		
		wi.set(u.scale(sample.x).add(v.scale(sample.y)).add(w.scale(sample.z)).normalize());
		shadeRec.pdf = shadeRec.normal.dot(wi) / Math.PI;
		
		return (cd.getColor(shadeRec).scale(kd / Math.PI));
	}

}
