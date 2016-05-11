package sampling;

import java.util.Random;

public class Jittered extends Sampler {

	public Jittered(int numberOfSamples, int numberOfSets, long seed) {
		super(numberOfSamples, numberOfSets, new Random(seed));
	}

	@Override
	public void generateSamples() {
		int n = (int) Math.sqrt(numberOfSamples);
		for(int i = 0; i < numberOfSets; i++){
			for(int row = 0; row < n; row++){
				for(int column = 0; column < n; column++){
					samples[(row*n+column)+i*numberOfSamples] = new Sample((column+random.nextDouble()) / n, (row+random.nextDouble()) / n);
				}
			}
		}
	}
	
	@Override
	public void shuffleSamples(){
		for(int i = 0; i < numberOfSets; i++){
			for (int j = 1; j < numberOfSamples; j++) {
				int replace = random.nextInt(j + 1);
				exchange(samples, (i * numberOfSamples) + replace, (i * numberOfSamples) + j);
			}
		}
	}
	
	private static void exchange(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
}
