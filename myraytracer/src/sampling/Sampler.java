package sampling;

import java.util.Random;

public abstract class Sampler {

	public Random random;
	public int numberOfSamples;
	public int numberOfSets;
	public Sample[] samples;
	public int count;
	
	public Sampler(int numberOfSamples, int numberOfSets, Random random){
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
		this.random = random;
		samples = new Sample[numberOfSamples * numberOfSets];
		generateSamples();
	}
	
	public abstract void generateSamples();
	public void shuffleSamples(){}
	public Sample getSampleUnitSquare(){
		return new Sample(samples[count++ % (numberOfSamples * numberOfSets)]);
	}
	
}
