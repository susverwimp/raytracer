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
	
	public Sampler(int numberOfSamples, int numberOfSets, Random random){
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
		this.random = random;
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
		return new Point3d(hemisphereSamples[count++ % (numberOfSamples * numberOfSets)]);
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
	
}
