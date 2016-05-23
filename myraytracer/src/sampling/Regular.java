package sampling;

public class Regular extends Sampler {

	public Regular() {
		super(0, 0, 0);
	}

	@Override
	public void generateSamples() {
	}
	
	@Override
	public Sample getSampleUnitSquare(){
		return new Sample(0.5, 0.5);
	}
}