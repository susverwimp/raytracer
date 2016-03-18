package material;

import brdf.SVGlossySpecular;
import brdf.SVLambertian;
import light.Light;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVPhong extends Material {

	private SVLambertian ambientBRDF;
	private SVLambertian diffuseBRDF;
	private SVGlossySpecular specularBRDF;
	
	public SVPhong(Texture texture) {
		ambientBRDF = new SVLambertian(texture);
		diffuseBRDF = new SVLambertian(texture);
		specularBRDF = new SVGlossySpecular(texture);
	}
	
	public void setKA(double ka){
		ambientBRDF.kd = ka;
	}
	
	public void setKD(double kd){
		diffuseBRDF.kd = kd;
	}
	
	public void setExp(double exp){
		specularBRDF.exp = exp;
	}
	
	public void setKS(double ks){
		specularBRDF.ks = ks;
	}
	
	public void setCD(Texture cd){
		ambientBRDF.cd = cd;
		diffuseBRDF.cd = cd;
		specularBRDF.cd = cd;
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
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo), L);
					RGBColor.add(specularBRDF.f(shadeRec, wi, wo).multiply(light.L(shadeRec).scale(nDotWi)), L);
				}
			}
		}
		return L;
	}

}
