package sampling;

public class Regular extends Sampler {

	public Regular() {
		super(0);
	}
	
	public Sample[] generateSamples(int totalSamples){
		Sample[] samples = new Sample[totalSamples];
		for (int i = 0; i < samples.length; i++) {
			samples[i] = new Sample(0.5, 0.5);
		}
		return samples;
	}

//	@Override
//	public void generateSamples() {
//	}
	
//	@Override
//	public Sample getSampleUnitSquare(){
//		return new Sample(0.5, 0.5);
//	}
}