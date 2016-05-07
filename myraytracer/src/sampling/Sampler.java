package sampling;

import java.util.Random;

public abstract class Sampler {

	public Random random;
	public int numberOfSamples;
	public int numberOfSets;
	public Sample[] samples;
	public int[] shuffeledIndices;
	public int count;
	public int jump;
	
	public Sampler(int numberOfSamples, int numberOfSets, Random random){
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
//		this.random = random;
		samples = new Sample[numberOfSamples * numberOfSets];
	}
	
	public abstract void generateSamples();
	public Sample getSampleUnitSquare(){
//		if(count % numberOfSamples == 0)
//			jump = (random.nextInt() % numberOfSets) * numberOfSamples;
		
		//return new Sample(samples[count++ % (numberOfSamples * numberOfSets)]);
//		return new Sample(samples[jump + shuffeledIndices[jump + count++ % numberOfSamples]]);
		return null;
	}
	
}
