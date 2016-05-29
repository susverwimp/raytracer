package sampling;

public class Jittered extends Sampler {

	public boolean shuffle;
	
//	public Jittered(int numberOfSamples, int numberOfSets, long seed) {
//		super(numberOfSamples, numberOfSets, seed);
//	}

//	@Override
//	public void generateSamples() {
//		int n = (int) Math.sqrt(numberOfSamples);
//		for (int i = 0; i < numberOfSets; i++) {
//			for (int row = 0; row < n; row++) {
//				for (int column = 0; column < n; column++) {
//					samples.push(new Sample((column + random.nextDouble()) / n, (row + random.nextDouble()) / n));
//					// samples[(row*n+column)+i*numberOfSamples] = new
//					// Sample((column+random.nextDouble()) / n,
//					// (row+random.nextDouble()) / n);
//				}
//			}
//		}
//	}

	public Jittered(long seed) {
		this(false, seed);
	}
	
	public Jittered(boolean shuffle, long seed){
		super(seed);
		this.shuffle = shuffle;
	}

	public Sample[] generateSamples(int totalSamples) {
		Sample[] samples = new Sample[totalSamples];
		int n = (int) Math.sqrt(totalSamples);
		for (int row = 0; row < n; row++) {
			for (int column = 0; column < n; column++) {
				samples[(row * n + column)] = new Sample((column + random.nextDouble()) / n,
						(row + random.nextDouble()) / n);
			}
		}
		if(shuffle)
			shuffle(samples);
		return samples;
	}
	
	private void shuffle(Object[] array) {
		int length = array.length;

		for (int i = 1; i < length; i++) {
			int replace = random.nextInt(i + 1);
			exchange(array, replace, i);
		}
	}

	private static void exchange(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
}
