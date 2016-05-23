package sampling;

import java.util.Random;

import math.Point3d;

public abstract class Sampler {

	public Random random;
	public int numberOfSamples;
	public int numberOfSets;
	public Sample[] samples;
	public Point3d[] hemisphereSamples;
	public int count;
	
	public Sampler(int numberOfSamples, int numberOfSets, long seed){
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
		this.random = new Random(seed);
		samples = new Sample[numberOfSamples * numberOfSets];
		hemisphereSamples = new Point3d[numberOfSamples * numberOfSets];
		generateSamples();
	}
	
	public abstract void generateSamples();
	public void shuffleSamples(){}
	public Sample getSampleUnitSquare(){
		return new Sample(samples[count++ % (numberOfSamples * numberOfSets)]);
	}
	
	public Point3d getSampleUnitHemisphere(){
		return new Point3d(hemisphereSamples[count++]);
//		return new Point3d(hemisphereSamples[count++ % (numberOfSamples * numberOfSets)]);
	}
	
	public void mapSamplesToHemisphere(double e){
		int index = 0;
		for(Sample sample : samples){
			double cosPhi = Math.cos(2.0 * Math.PI * sample.x);
			double sinPhi = Math.sin(2.0 * Math.PI * sample.x);
			double cosTheta = Math.pow((1.0 - sample.y), 1.0 / (e + 1.0));
			double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
			double pu = sinTheta * cosPhi;
			double pv = sinTheta * sinPhi;
			double pw = cosTheta;
			
			hemisphereSamples[index++] = new Point3d(pu, pv, pw);
		}
	}
	
	public void mapSamplesToCosineHemisphere(){
		int index = 0;
		for(Sample sample : samples){
			double r = Math.sqrt(sample.x);
			double theta = 2 * Math.PI * sample.y;
		 
		    double x = r * Math.cos(theta);
		    double y = r * Math.sin(theta);
		 
		    hemisphereSamples[index++] = new Point3d(x, y, Math.sqrt(Math.max(0.0f, 1 - sample.x)));
		}
	}
	
}
