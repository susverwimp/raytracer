package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {

	public static void main(String[] args) throws IOException {
		BufferedImage[] images = new BufferedImage[11];
		for (int i = 0; i < images.length; i++) {
			images[i] = ImageIO.read(new File("res/renders/test/path_cornell_256_10_1_small_" + i + ".png"));
		}

		int width = images[0].getWidth();
		int height = images[0].getHeight();
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		for (int i = 0; i < images.length; i++) {
			g.drawImage(images[i], 0, 0, null);
		}

		try {
			ImageIO.write(result, "png", new File("test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
		System.out.println("complete");
	}

}
