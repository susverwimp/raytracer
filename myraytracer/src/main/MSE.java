package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MSE {

	public static void main(String[] args) throws IOException {
		BufferedImage image1 = ImageIO.read(new File("res/renders/cornell/path_cornell_2916_10_1_big.png"));
		BufferedImage image2 = ImageIO.read(new File("res/renders/cornell/path_cornell_14400_10_1_big.png"));
		int width = image1.getWidth();
		int height = image1.getHeight();
		double mse;
		double sum = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p1 = image1.getRGB(i, j);
				double r1 = ((p1 & 0x00ff0000) >> 16) / 255.0;
				double g1 = ((p1 & 0x0000ff00) >> 8) / 255.0;
				double b1 =  (p1 & 0x000000ff) / 255.0;
				int p2 = image2.getRGB(i, j);
				double r2 = ((p2 & 0x00ff0000) >> 16) / 255.0;
				double g2 = ((p2 & 0x0000ff00) >> 8) / 255.0;
				double b2 =  (p2 & 0x000000ff) / 255.0;
				double err = (r1 - r2) + (g1 - g2) + (b1 - b2);
				sum += (err * err);
			}
		}
		mse = sum / (width * height);
		System.out.println(mse);
	}

}
