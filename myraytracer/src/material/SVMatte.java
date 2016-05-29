package material;

import brdf.SVLambertian;
import light.Light;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;
import world.World;

public class SVMatte extends Material {

	private SVLambertian ambientBRDF;
	private SVLambertian diffuseBRDF;

	public SVMatte(Texture texture) {
		ambientBRDF = new SVLambertian(texture);
		diffuseBRDF = new SVLambertian(texture);
	}

	public void setKA(double ka) {
		ambientBRDF.kd = ka;
	}

	public void setKD(double kd) {
		diffuseBRDF.kd = kd;
	}

	public void setCD(Texture cd) {
		ambientBRDF.cd = cd;
		diffuseBRDF.cd = cd;
	}

	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.rho(shadeRec, wo).scale(shadeRec.world.ambient.L(shadeRec));
		for (Light light : shadeRec.world.lights) {
			Vector3d wi = light.getDirection(shadeRec);
			double nDotWi = shadeRec.normal.dot(wi) / (shadeRec.normal.length() * wi.length());
			if (nDotWi > 0.0) {
				boolean inShadow = false;
				if (light.castShadows) {
					Ray shadowRay = new Ray(shadeRec.hitPoint, wi);
					inShadow = light.inShadow(shadowRay, shadeRec);
				}
				if (!inShadow) {
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
		for (Light light : shadeRec.world.lights) {
			Vector3d wi = light.getDirection(shadeRec);
			double nDotWi = shadeRec.normal.dot(wi);
			if (nDotWi > 0.0) {
				boolean inShadow = false;
				if (light.castShadows) {
					Ray shadowRay = new Ray(shadeRec.hitPoint, wi);
					inShadow = light.inShadow(shadowRay, shadeRec);
				}
				if (!inShadow) {
					RGBColor.add(diffuseBRDF.f(shadeRec, wi, wo).scale(light.L(shadeRec)).scale(light.G(shadeRec))
							.scale(nDotWi / light.pdf(shadeRec)), L);
				}
			}
		}
		return L;
	}

	@Override
	public RGBColor pathShade(ShadeRec shadeRec) {
		Vector3d wo = shadeRec.ray.direction.scale(-1);
		RGBColor result = new RGBColor();
		for (int i = 0; i < World.BRANCHING_FACTOR; i++) {
			Vector3d wi = new Vector3d();
			RGBColor f = diffuseBRDF.sampleF(shadeRec, wo, wi);
			// double nDotWi = shadeRec.normal.dot(wi);
			Ray reflectedRay = new Ray(shadeRec.hitPoint, wi);
			// RGBColor.add((f.scale(shadeRec.world.tracer.traceRay(reflectedRay,
			// shadeRec.materialSampler, shadeRec.depth + 1).scale(nDotWi /
			// shadeRec.pdf))), result);
			RGBColor.add((f.scale(
					shadeRec.world.tracer.traceRay(reflectedRay, shadeRec.arealightSampler, shadeRec.materialSampler, shadeRec.depth + 1))),
					result);
		}

		// get average of the samples
		RGBColor.scale(1.0 / World.BRANCHING_FACTOR, result);
		return result;
		// return (f.scale(shadeRec.world.tracer.traceRay(reflectedRay, null,
		// shadeRec.depth + 1, 1).scale(nDotWi / shadeRec.pdf)));
	}

	// @Override
	// public RGBColor hybridPathShade(ShadeRec shadeRec) {
	// Vector3d wo = shadeRec.ray.direction.scale(-1);
	// RGBColor result = new RGBColor();
	//
	// //calculate indirect radiance
	// RGBColor indirectRadiance = new RGBColor();
	// for(int i = 0; i < World.BRANCHING_FACTOR; i++){
	// Vector3d wi = new Vector3d();
	// RGBColor f = diffuseBRDF.sampleF(shadeRec, wo, wi);
	//// double nDotWi = shadeRec.normal.dot(wi);
	// Ray reflectedRay = new Ray(shadeRec.hitPoint, wi);
	//// RGBColor.add((f.scale(shadeRec.world.tracer.traceRay(reflectedRay,
	// shadeRec.materialSampler, shadeRec.depth + 1).scale(nDotWi /
	// shadeRec.pdf))), result);
	// RGBColor.add((f.scale(shadeRec.world.tracer.traceRay(reflectedRay,
	// shadeRec.materialSampler, shadeRec.depth + 1))), indirectRadiance);
	// }
	// RGBColor.scale(1.0 / (World.BRANCHING_FACTOR), indirectRadiance);
	//
	// //calculate direct radiance
	// RGBColor directRadiance = new RGBColor();
	// for(Light light : shadeRec.world.lights){
	// Vector3d wi = light.getDirection(shadeRec);
	// RGBColor f = diffuseBRDF.f(shadeRec, wo, wi);
	// double nDotWi = shadeRec.normal.dot(wi);
	// if(nDotWi > 0.0){
	// boolean inShadow = false;
	// if(light.castShadows){
	// Ray shadowRay = new Ray(shadeRec.hitPoint, wi);
	// inShadow = light.inShadow(shadowRay, shadeRec);
	// }
	// if(!inShadow){
	// double lightNormalDotWiLight = shadeRec.lightNormal.scale(-1).dot(wi);
	// if(lightNormalDotWiLight > 0.0){
	//// shadeRec.ray = new Ray(shadeRec.hitPoint, wi);
	//// RGBColor.add(f.scale(((AreaLight)light).object.material.getLE(shadeRec).scale(nDotWi/shadeRec.pdf))
	// , result);
	// RGBColor.add(f.scale(((AreaLight)light).object.material.hybridPathShade(shadeRec).scale(nDotWi/Math.PI))
	// , directRadiance);
	// }
	// }
	// }
	// }
	//
	//
	// // get average of the samples
	// RGBColor.scale(1.0 / (World.BRANCHING_FACTOR +
	// shadeRec.world.lights.size()), result);
	// return result;
	// }

	@Override
	public RGBColor hybridPathShade(ShadeRec shadeRec) {
		
		RGBColor L = new RGBColor();

		// calculate indirect radiance
		RGBColor indirectRadiance = pathShade(shadeRec);
		
		// calculate direct radiance
		RGBColor directRadiance = areaLightShade(shadeRec);

		// sum up the total radiance
		RGBColor.add(indirectRadiance, L);
		RGBColor.add(directRadiance, L);

		// get average of the samples
		return L;
	}
}