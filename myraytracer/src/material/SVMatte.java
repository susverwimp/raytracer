package material;

import brdf.SVLambertian;
import light.Light;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVMatte extends Material {
	
	private SVLambertian ambientBRDF;
	private SVLambertian diffuseBRDF;
	
	public SVMatte(Texture texture){
		ambientBRDF = new SVLambertian(texture);
		diffuseBRDF = new SVLambertian(texture);
	}
	
	public void setKA(double ka){
		ambientBRDF.kd = ka;
	}
	
	public void setKD(double kd){
		diffuseBRDF.kd = kd;
	}
	
	public void setCD(Texture cd){
		ambientBRDF.cd = cd;
		diffuseBRDF.cd = cd;
	}

	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.rho(shadeRec, wo).multiply(shadeRec.world.ambient.L(shadeRec));
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

	@Override
	public RGBColor areaLightShade(ShadeRec shadeRec) {
//		Vector3d wo = shadeRec.ray.direction.scale(-1);
//		RGBColor L = ambientBRDF.rho(shadeRec, wo).multiply(shadeRec.world.ambient.L(shadeRec));
//		for(Light light : shadeRec.world.lights){
//			Vector3d wi = light.getDirection(shadeRec);
//			double nDotWi = shadeRec.normal.dot(wi) / (shadeRec.normal.length() * wi.length());
//			if(nDotWi > 0.0){
//				boolean inShadow = false;
//				if(light.castShadows){
//					Ray shadowRay = new Ray(shadeRec.hitPoint, wi);
//					inShadow = light.inShadow(shadowRay, shadeRec);
//				}
//				if(!inShadow){
//					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).multiply(light.L(shadeRec).scale(nDotWi)), L);
//				}
//			}
//		}
//		return L;
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.rho(shadeRec, wo).multiply(shadeRec.world.ambient.L(shadeRec));
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
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).multiply(light.L(shadeRec)).multiply(light.G(shadeRec) * nDotWi).multiply(1.0/light.pdf(shadeRec)) , L);
				}
			}
		}
		return L;
	}
	
	

}
