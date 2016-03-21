package math;

import java.util.Locale;

public class RGBColor {

	public double r;
	public double g;
	public double b;

	public RGBColor() {
		this(0, 0, 0);
	}

	public RGBColor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public RGBColor(RGBColor color){
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
	}
	
	public RGBColor scale(double scalar){
		return new RGBColor(r*scalar, g*scalar, b*scalar);
	}
	
	public static void scale(double scalar, RGBColor destination){
		destination.r *= scalar;
		destination.g *= scalar;
		destination.b *= scalar;
	}
	
	public static void add(RGBColor color, RGBColor destination){
		destination.r += color.r;
		destination.g += color.g;
		destination.b += color.b;
	}
	
	public RGBColor multiply(RGBColor color){
		return new RGBColor(r * color.r, g * color.g, b * color.b);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[%s]:\n%g %g %g", getClass()
				.getName(), r, g, b);
	};

}
