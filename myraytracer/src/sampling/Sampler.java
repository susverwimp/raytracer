package sampling;

import java.util.Random;

public abstract class Sampler {

	public Random random;
	public int numberOfSamples;
	public int numberOfSets;
	public Sample[] samples;
	public int count;
	public int jump;
	
	public Sampler(int numberOfSamples, int numberOfSets){
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
		samples = new Sample[numberOfSamples * numberOfSets];
	}
	
	public abstract void generateSamples();
	public Sample getSampleUnitSquare(){
		return new Sample(samples[count++ % (numberOfSamples * numberOfSets)]);
	}
	
}
