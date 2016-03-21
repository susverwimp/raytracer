package sampling;

import java.util.Random;

public class Jittered extends Sampler {

	public Random random;
	
	public Jittered(int numberOfSamples, int numberOfSets, long seed) {
		super(numberOfSamples, numberOfSets);
		random = new Random(seed);
	}

	@Override
	public void generateSamples() {
		int n = (int) Math.sqrt(numberOfSamples);
		
		for(int i = 0; i < numberOfSets; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n; k++){
					samples[(j*n+k)+i*numberOfSamples] = new Sample((k+random.nextDouble()) / n, (j+random.nextDouble()) / n);
				}
			}
		}
	}

	@Override
	public Sample getSampleUnitSquare() {
		return new Sample(samples[count++]);
	}
}
