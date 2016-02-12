package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import math.Point;
import math.RGBColor;
import math.Ray;
import math.ShadeRec;
import math.Transformation;
import math.Vector;
import sampling.Sample;
import shape.GeometricObject;
import shape.Sphere;
import camera.PerspectiveCamera;
import film.FrameBuffer;
import film.Tile;
import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;
import light.Light;
import light.PointLight;

/**
 * Entry point of your renderer.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Renderer {
	/**
	 * Entry point of your renderer.
	 * 
	 * @param arguments
	 *            command line arguments.
	 */
	
	public final static RGBColor BACKGROUND_COLOR = new RGBColor(0, 0, 0);
	
	public static void main(String[] arguments) {
		int width = 640;
		int height = 640;
		double sensitivity = 1.0;
		double gamma = 2.2;
		boolean gui = true;

		/**********************************************************************
		 * Parse the command line arguments
		 *********************************************************************/

		for (int i = 0; i < arguments.length; ++i) {
			if (arguments[i].startsWith("-")) {
				try {
					if (arguments[i].equals("-width"))
						width = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-height"))
						height = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-gui"))
						gui = Boolean.parseBoolean(arguments[++i]);
					else if (arguments[i].equals("-sensitivity"))
						sensitivity = Double.parseDouble(arguments[++i]);
					else if (arguments[i].equals("-gamma"))
						gamma = Double.parseDouble(arguments[++i]);
					else if (arguments[i].equals("-help")) {
						System.out
								.println("usage: "
										+ "[-width  <integer> width of the image] "
										+ "[-height  <integer> height of the image] "
										+ "[-sensitivity  <double> scaling factor for the radiance] "
										+ "[-gamma  <double> gamma correction factor] "
										+ "[-gui  <boolean> whether to start a graphical user interface]");
						return;
					} else {
						System.err.format("unknown flag \"%s\" encountered!\n",
								arguments[i]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.format("could not find a value for "
							+ "flag \"%s\"\n!", arguments[i]);
				}
			} else
				System.err.format("unknown value \"%s\" encountered! "
						+ "This will be skipped!\n", arguments[i]);
		}

		/**********************************************************************
		 * Validate the input
		 *********************************************************************/

		if (width <= 0)
			throw new IllegalArgumentException("the given width cannot be "
					+ "smaller than or equal to zero!");
		if (height <= 0)
			throw new IllegalArgumentException("the given height cannot be "
					+ "smaller than or equal to zero!");
		if (gamma <= 0)
			throw new IllegalArgumentException("the gamma cannot be "
					+ "smaller than or equal to zero!");
		if (sensitivity <= 0)
			throw new IllegalArgumentException("the sensitivity cannot be "
					+ "smaller than or equal to zero!");

		/**********************************************************************
		 * Initialize the camera and graphical user interface
		 *********************************************************************/

		final PerspectiveCamera camera = new PerspectiveCamera(width, height,
				new Point(0, 0, 0), new Point(0, 0, 1), new Vector(0, 1, 0), 90);

		// initialize the frame buffer
		final FrameBuffer buffer = new FrameBuffer(width, height);

		// initialize the progress reporter
		final ProgressReporter reporter = new ProgressReporter("Rendering", 40,
				width * height, false);

		// initialize the graphical user interface if desired
		final ImagePanel panel;
		if (gui) {
			panel = new ImagePanel(width, height, sensitivity, gamma);
			RenderFrame frame = new RenderFrame("Spheres", panel);
			reporter.addProgressListener(frame);
		} else
			panel = null;

		/**********************************************************************
		 * Initialize the scene
		 *********************************************************************/

		Transformation t1 = Transformation.translate(0, 0, 10).append(
				Transformation.scale(5, 5, 5));
		Transformation t2 = Transformation.translate(4, -4, 12).append(
				Transformation.scale(4, 4, 4));
		Transformation t3 = Transformation.translate(-4, -4, 12).append(
				Transformation.scale(4, 4, 4));
		Transformation t4 = Transformation.translate(4, 4, 12).append(
				Transformation.scale(4, 4, 4));
		Transformation t5 = Transformation.translate(-4, 4, 12).append(
				Transformation.scale(4, 4, 4));

		final List<GeometricObject> shapes = new ArrayList<>();
		shapes.add(new Sphere(t1));
		shapes.add(new Sphere(t2));
		shapes.add(new Sphere(t3));
		shapes.add(new Sphere(t4));
		shapes.add(new Sphere(t5));
		
		final List<Light> lights = new ArrayList<>();
		lights.add(new PointLight(new RGBColor(0.3, 0.8, 0.1), 3, new Point(0, 10, 0)));

		/**********************************************************************
		 * Multi-threaded rendering of the scene
		 *********************************************************************/

		final ExecutorService service = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());

		// subdivide the buffer in equal sized tiles
		for (final Tile tile : buffer.subdivide(64, 64)) {
			// create a thread which renders the specific tile
			Thread thread = new Thread() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					//create 1 shadeRecord
					ShadeRec shadeRec = new ShadeRec();
					
					// iterate over the contents of the tile
					for (int y = tile.yStart; y < tile.yEnd; ++y) {
						for (int x = tile.xStart; x < tile.xEnd; ++x) {
							// create a ray through the center of the
							// pixel.
							Ray ray = camera.generateRay(new Sample(x + 0.5,
									y + 0.5));

							// test the scene on intersections
							for (GeometricObject shape : shapes){
								shape.intersect(ray, shadeRec);
							}

							// add a color contribution to the pixel
							if(shadeRec.isHit){
								//calculate color of shadeRec
								for(Light light : lights){
									Vector lightDirection = ((PointLight)light).origin.subtract(shadeRec.localHitPoint);
									double cosinFactor = shadeRec.normal.dot(lightDirection) / (shadeRec.normal.length() * lightDirection.length());
									shadeRec.color.r += 0.4*0.7*light.intensity*light.color.r*cosinFactor/Math.PI;
									if(shadeRec.color.r > 1)
										shadeRec.color.r = 1;
									shadeRec.color.g += 0.4*0.2*light.intensity*light.color.g*cosinFactor/Math.PI;
									if(shadeRec.color.g > 1)
										shadeRec.color.g = 1;
									shadeRec.color.b += 0.4*0.8*light.intensity*light.color.b*cosinFactor/Math.PI;
									if(shadeRec.color.b > 1)
										shadeRec.color.b = 1;
								}
								//draw pixel on panel
								buffer.getPixel(x, y).add(shadeRec.color);
								
								//set shadeRec isHit to false and the default color to zero for the next pixel
								shadeRec.isHit = false;
								shadeRec.color.set(0, 0, 0);
								shadeRec.tMin = ShadeRec.tMax;
							}else{
								//draw backgroundcolor on panel
								buffer.getPixel(x, y).add(BACKGROUND_COLOR);
							}
						}
					}

					// update the graphical user interface
					if (panel != null)
						panel.update(tile);

					// update the progress reporter
					reporter.update(tile.getWidth() * tile.getHeight());
				}
			};
			service.submit(thread);
		}

		// execute the threads
		service.shutdown();

		// wait until the threads have finished
		try {
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// signal the reporter that the task is done
		reporter.done();

		/**********************************************************************
		 * Export the result
		 *********************************************************************/

		BufferedImage result = buffer.toBufferedImage(sensitivity, gamma);
		try {
			ImageIO.write(result, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
