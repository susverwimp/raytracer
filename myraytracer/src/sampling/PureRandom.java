package sampling;

public class PureRandom extends Sampler {

//	public PureRandom(int numberOfSamples, int numberOfSets, long seed) {
//		super(numberOfSamples, numberOfSets, seed);
//	}
	
	public PureRandom(long seed){
		super(seed);
	}
	
//	@Override
//	public void generateSamples() {
//		for(int i = 0; i < numberOfSamples * numberOfSets; i++){
//			samples.push(new Sample(random.nextDouble(), random.nextDouble()));
////			samples[i] = new Sample(random.nextDouble(), random.nextDouble());
//		}
//	}
	
	public Sample[] generateSamples(int totalSamples){
		Sample[] samples = new Sample[totalSamples];
		for (int i = 0; i < samples.length; i++) {
			samples[i] = new Sample(random.nextDouble(), random.nextDouble());
		}
		return samples;
	}
	
}