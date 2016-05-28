package material;

import math.RGBColor;
import util.ShadeRec;
import world.World;

public class Emissive extends Material {

	public double ls;
	public RGBColor ce = new RGBColor();
	
	public Emissive(){
	}
	
	public void scaleRadiance(double ls){
		this.ls = ls;
	}
	
	public void setCE(double r, double g, double b){
		ce.r = r;
		ce.g = g;
		ce.b = b;
	}
	
	public RGBColor getLE(ShadeRec shadeRec){
		return ce.scale(ls);
	}
	
	@Override
	public RGBColor shade(ShadeRec shadeRec) {
		return null;
	}

	@Override
	public RGBColor areaLightShade(ShadeRec shadeRec) {
		if(shadeRec.normal.scale(-1).dot(shadeRec.ray.direction) > 0.0){
			return ce.scale(ls);
		}
		return World.BACKGROUND_COLOR;
	}

	@Override
	public RGBColor pathShade(ShadeRec shadeRec) {
		return ce.scale(ls);
	}

	@Override
	public RGBColor hybridPathShade(ShadeRec shadeRec) {
		if(shadeRec.depth == 0 || shadeRec.ray.originatingMaterial instanceof SVReflective){
			return ce.scale(ls);
		}
		return World.BACKGROUND_COLOR;
	}

}
