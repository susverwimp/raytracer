package brdf;

import math.RGBColor;
import math.Vector3d;
import sampling.Sampler;
import util.ShadeRec;

public abstract class BRDF {
	
	protected Sampler sampler;
	
	public abstract RGBColor f(ShadeRec shadeRec, Vector3d wi, Vector3d wo);
	public abstract RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo);
	public abstract RGBColor rho(ShadeRec shadeRec, Vector3d wo);

	public void setSampler(Sampler sampler){
		this.sampler = sampler;
	}
	
}
