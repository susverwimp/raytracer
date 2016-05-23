package sampling;

public class PureRandom extends Sampler {

	public PureRandom(int numberOfSamples, int numberOfSets, long seed) {
		super(numberOfSamples, numberOfSets, seed);
	}
	
	@Override
	public void generateSamples() {
		for(int i = 0; i < numberOfSamples * numberOfSets; i++){
			samples[i] = new Sample(random.nextDouble(), random.nextDouble());
		}
	}
	
}