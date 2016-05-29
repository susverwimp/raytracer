package sampling;

import java.util.Random;

import math.Point3d;

public abstract class Sampler {

	public Random random;
//	public int numberOfSamples;
//	public int numberOfSets;
//	public Stack<Sample> samples;
//	public Stack<Point3d> hemisphereSamples;
//	public int count;
	
//	public Sampler(int numberOfSamples, int numberOfSets, long seed){
////		this.numberOfSamples = numberOfSamples;
////		this.numberOfSets = numberOfSets;
//		this.random = new Random(seed);
////		samples = new Sample[numberOfSamples * numberOfSets];
////		hemisphereSamples = new Point3d[numberOfSamples * numberOfSets];
//		samples = new Stack<>();
//		hemisphereSamples = new Stack<>();
//		generateSamples();
//	}
	
	public Sampler(long seed){
		this.random = new Random(seed);
	}
	
//	public abstract void generateSamples();
	public Sample[] generateSamples(int totalSamples){
		return null;
	}
//	public void shuffleSamples(){
//		Collections.shuffle(samples);
//	}
//	public Sample getSampleUnitSquare(){
////		return new Sample(samples[count++ % (numberOfSamples * numberOfSets)]);
//		return samples.pop();
//	}
//	
//	public Point3d getSampleUnitHemisphere(){
//		return hemisphereSamples.pop();
////		return new Point3d(hemisphereSamples[count++]);
////		return new Point3d(hemisphereSamples[count++ % (numberOfSamples * numberOfSets)]);
//	}
//	
//	public void mapSamplesToHemisphere(double e){
//		for(Sample sample : samples){
//			double cosPhi = Math.cos(2.0 * Math.PI * sample.x);
//			double sinPhi = Math.sin(2.0 * Math.PI * sample.x);
//			double cosTheta = Math.pow((1.0 - sample.y), 1.0 / (e + 1.0));
//			double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
//			double pu = sinTheta * cosPhi;
//			double pv = sinTheta * sinPhi;
//			double pw = cosTheta;
//			
//			hemisphereSamples.push(new Point3d(pu, pv, pw));
////			hemisphereSamples[index++] = new Point3d(pu, pv, pw);
//		}
//	}
	
	public Point3d[] mapSamplesToCosineHemisphere(Sample[] samples){
		int length = samples.length;
		Point3d[] points = new Point3d[length];
		for (int i = 0; i < length; i++) {
			Sample sample = samples[i];
			double r = Math.sqrt(sample.x);
			double theta = 2 * Math.PI * sample.y;
		 
		    double x = r * Math.cos(theta);
		    double y = r * Math.sin(theta);
		    
		    points[i] = new Point3d(x, y, Math.sqrt(Math.max(0.0f, 1 - sample.x)));
		}
		return points;
	}
	
//	public void mapSamplesToCosineHemisphere(){
//		int size = samples.size();
//		for(int i = 0; i < size; i++){
//			Sample sample = samples.pop();
//			double r = Math.sqrt(sample.x);
//			double theta = 2 * Math.PI * sample.y;
//		 
//		    double x = r * Math.cos(theta);
//		    double y = r * Math.sin(theta);
//		 
//		    hemisphereSamples.push(new Point3d(x, y, Math.sqrt(Math.max(0.0f, 1 - sample.x))));
//		}
////		for(Sample sample : samples){
////			double r = Math.sqrt(sample.x);
////			double theta = 2 * Math.PI * sample.y;
////		 
////		    double x = r * Math.cos(theta);
////		    double y = r * Math.sin(theta);
////		 
////		    hemisphereSamples.push(new Point3d(x, y, Math.sqrt(Math.max(0.0f, 1 - sample.x))));
//////		    hemisphereSamples[index++] = new Point3d(x, y, Math.sqrt(Math.max(0.0f, 1 - sample.x)));
////		}
//	}
	
}
