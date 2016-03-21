package sampling;

public class Regular extends Sampler {

	public Regular(int numberOfSamples, int numberOfSets) {
		super(numberOfSamples, numberOfSets);
	}

	@Override
	public void generateSamples() {
		for(int i = 0; i < numberOfSets; i++){
			for(int j = 0; j < numberOfSamples; j++){
				samples[j+i*numberOfSamples] = new Sample(0.5, 0.5);
			}
		}
	}
}