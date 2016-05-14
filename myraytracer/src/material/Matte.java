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
		RGBColor L = ambientBRDF.rho(shadeRec, wo).scale(shadeRec.world.ambient.L(shadeRec));
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
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).scale(light.L(shadeRec).scale(nDotWi)), L);
				}
			}
		}
		return L;
	}

	@Override
	public RGBColor areaLightShade(ShadeRec shadeRec) {
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.rho(shadeRec, wo).scale(shadeRec.world.ambient.L(shadeRec));
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
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).scale(light.L(shadeRec)).scale(light.G(shadeRec) * nDotWi).scale(1.0/light.pdf(shadeRec)) , L);
				}
			}
		}
		return L;
	}

	@Override
	public RGBColor pathShade(ShadeRec shadeRec) {
		Vector3d wi = new Vector3d();
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor f = diffuseBRDF.sampleF(shadeRec, wo, wi);
		double nDotWi = shadeRec.normal.dot(wi);
		Ray reflectedRay = new Ray(shadeRec.hitPoint, wi);
		
		return (f.scale(shadeRec.world.tracer.traceRay(reflectedRay, null, shadeRec.depth + 1, 1).scale(nDotWi / shadeRec.pdf)));
	}

}
