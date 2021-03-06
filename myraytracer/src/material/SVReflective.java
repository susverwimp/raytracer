package material;

import brdf.SVPerfectSpecular;
import math.RGBColor;
import math.Ray;
import math.Vector3d;
import texture.Texture;
import util.ShadeRec;

public class SVReflective extends Material {

	public SVPerfectSpecular reflectiveBRDF;

	public SVReflective(Texture texture) {
		reflectiveBRDF = new SVPerfectSpecular(texture);
	}

	public void setKR(double kr) {
		reflectiveBRDF.kr = kr;
	}

	public void setCR(Texture cr) {
		reflectiveBRDF.cr = cr;
	}
	
	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		return null;
	}

	@Override
	public RGBColor areaLightShade(ShadeRec shadeRec) {
		return null;
	}

	@Override
	public RGBColor pathShade(ShadeRec shadeRec) {
		RGBColor L = new RGBColor();

		Vector3d wo = shadeRec.ray.direction.scale(-1);
		Vector3d wi = new Vector3d();
		RGBColor f = reflectiveBRDF.sampleF(shadeRec, wi, wo);
		double nDotWi = shadeRec.normal.dot(wi);
		Ray reflectedRay = new Ray(shadeRec.hitPoint, wi);
		reflectedRay.originatingMaterial = this;
		RGBColor.add((f.scale(shadeRec.world.tracer.traceRay(reflectedRay, shadeRec.arealightSampler,
				shadeRec.materialSampler, shadeRec.depth + 1))).scale(nDotWi / shadeRec.pdf), L);
//		RGBColor.add(f.scale(shadeRec.world.tracer.traceRay(reflectedRay, shadeRec.arealightSampler,
//				shadeRec.materialSampler, shadeRec.depth + 1)).scale(shadeRec.normal.dot(wi) / shadeRec.pdf), L);

		return L;
	}

	@Override
	public RGBColor hybridPathShade(ShadeRec shadeRec) {
		return pathShade(shadeRec);
	}

}
