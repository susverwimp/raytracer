package sampling;

import java.util.Random;

public class PureRandom extends Sampler {

	public Random random;
	
	public PureRandom(int numberOfSamples, int numberOfSets, long seed) {
		super(numberOfSamples, numberOfSets, new Random(seed));
		numberOfSets = 1;
	}

	@Override
	public void generateSamples() {
		count = 0;
		for(int i = 0; i < numberOfSamples; i++){
			samples[i] = new Sample(random.nextDouble(), random.nextDouble());
		}
	}

}
