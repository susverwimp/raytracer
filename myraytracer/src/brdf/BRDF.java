package brdf;

import math.RGBColor;
import math.Vector3d;
import sampling.Sampler;
import util.ShadeRec;
import world.World;

public abstract class BRDF {
	
	protected Sampler sampler;
	
	public RGBColor f(ShadeRec shadeRec, Vector3d wi, Vector3d wo){
		return World.BACKGROUND_COLOR;
	}
	public abstract RGBColor sampleF(ShadeRec shadeRec, Vector3d wi, Vector3d wo);
	public RGBColor rho(ShadeRec shadeRec, Vector3d wo){
		return World.BACKGROUND_COLOR;
	}

	public void setSampler(Sampler sampler){
		this.sampler = sampler;
	}
	
}
