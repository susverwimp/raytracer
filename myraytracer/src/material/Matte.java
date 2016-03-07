package material;

import brdf.Lambertian;
import light.Light;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import util.ShadeRec;

public class Matte extends Material {

	private Lambertian ambientBRDF;
	private Lambertian diffuseBRDF;
	
	public Matte(){
		ambientBRDF = new Lambertian();
		diffuseBRDF = new Lambertian();
	}
	
	public void setKA(double ka){
		ambientBRDF.kd = ka;
	}
	
	public void setKD(double kd){
		diffuseBRDF.kd = kd;
	}
	
	public void setCD(RGBColor c){
		ambientBRDF.cd = c;
		diffuseBRDF.cd = c;
	}
	
	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.rho(shadeRec, wo).multiply(shadeRec.world.ambient.L(shadeRec));
//		RGBColor L = new RGBColor();
		for(Light light : shadeRec.world.lights){
			Vector3d wi = light.getDirection(shadeRec);
			double nDotWi = shadeRec.normal.dot(wi) / (shadeRec.normal.length() * wi.length());
			if(nDotWi > 0.0){
				boolean inShadow = false;
				if(light.castShadows){
					Ray shadowRay = new Ray(shadeRec.hitPoint, wi);
					inShadow = light.inShadow(shadowRay, shadeRec);
				}
				if(!inShadow){
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).multiply(light.L(shadeRec).scale(nDotWi)), L);
				}
			}
		}
		return L;
	}

}
