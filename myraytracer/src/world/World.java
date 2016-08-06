package world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import camera.Camera;
import film.FrameBuffer;
import film.Tile;
import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;
import light.Ambient;
import light.Light;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Vector2d;
import math.Vector3d;
import sampling.Jittered;
import sampling.PureRandom;
import sampling.Regular;
import sampling.Sample;
import sampling.Sampler;
import shape.GeometricObject;
import tracer.AreaLighting;
import tracer.HybridPathTracing;
import tracer.PathTracer;
import tracer.Tracer;
import util.ShadeRec;

public class World {

	private final FrameBuffer buffer;
	private final ProgressReporter reporter;
	private final ImagePanel panel;

	public Camera camera;
	public Tracer tracer;
	public static final int MAX_BOUNCES = 10;
	public static final int SAMPLES_PER_PIXEL = 16;
	public static final int BRANCHING_FACTOR = 1;
	public static final RGBColor BACKGROUND_COLOR = new RGBColor();
	public static final int SHOW_BOUNCE = -1;
	public Light ambient = new Ambient();
	public final List<GeometricObject> shapes = new ArrayList<>();
	public final List<Light> lights = new ArrayList<>();

	private static final RGBColor falseColor1 = new RGBColor(0, 0, 0);
	private static final RGBColor falseColor2 = new RGBColor(1, 1, 1);

	private static final boolean OUT_OF_GAMUT = true;
	

	public World(int width, int height, double sensitivity, double gamma, boolean gui) throws IOException {
		/**********************************************************************
		 * Initialize the camera and graphical user interface
		 *********************************************************************/
		

		// initialize the frame buffer
		buffer = new FrameBuffer(width, height);

		// initialize the progress reporter
		reporter = new ProgressReporter("Rendering", 40,
				width * height, false);
		
		// initialize the graphical user interface if desired
		if (gui) {
			panel = new ImagePanel(width, height, sensitivity, gamma);
			RenderFrame frame = new RenderFrame("Sus Verwimp Ray Tracer", panel);
			reporter.addProgressListener(frame);
		} else
			panel = null;
		
		WorldBuilder.build(WorldBuilder.CORNELL_BOX_PATH_TRACING, width, height, false, this);
	}

	public void renderScene() {
		/**********************************************************************
		 * Multi-threaded rendering of the scene
		 *********************************************************************/

		final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		// subdivide the buffer in equal sized tiles
		for (final Tile tile : buffer.subdivide(64,64)) {
			// create a thread which renders the specific tile
			Thread thread = new Thread() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					try {
						Sampler pixelSampler = null;
						Sampler arealightSampler = null;
						Sampler materialSampler = null;
						if(SAMPLES_PER_PIXEL == 1){
							pixelSampler = new Regular();
							if(tracer instanceof AreaLighting)
								arealightSampler = new Regular();
							else if(tracer instanceof PathTracer || tracer instanceof HybridPathTracing){
//								int totalSamples = 0;
//								if(BRANCHING_FACTOR <= 1)
//									totalSamples = MAX_BOUNCES + 1;
//								else
//									totalSamples = (int)((Math.pow(BRANCHING_FACTOR,(MAX_BOUNCES+1))-1) / (BRANCHING_FACTOR-1))-1;

								materialSampler = new PureRandom(tile.xStart + tile.getWidth() * tile.yStart);
//								materialSampler = new PureRandom(totalSamples, tile.getWidth() * tile.getHeight(), tile.xStart + tile.getWidth() * tile.yStart);
//								materialSampler.mapSamplesToCosineHemisphere();
								
								if(tracer instanceof HybridPathTracing){
									arealightSampler = new PureRandom(tile.xStart + tile.getWidth() * tile.yStart);
//									arealightSampler = new PureRandom(totalSamples, tile.getWidth() * tile.getHeight(), tile.xStart + tile.getWidth() * tile.yStart);
								}
							}
						}else{
//							pixelSampler = new Jittered(SAMPLES_PER_PIXEL, tile.getWidth() * tile.getHeight(),  tile.xStart + tile.getWidth() * tile.yStart);
							pixelSampler = new Jittered(tile.xStart + tile.getWidth() * tile.yStart);
							if(tracer instanceof AreaLighting){
								arealightSampler = new PureRandom(tile.xStart + tile.getWidth() * tile.yStart);
//								arealightSampler = new Jittered(SAMPLES_PER_PIXEL, tile.getWidth() * tile.getHeight() * lights.size(), tile.xStart + tile.getWidth() * tile.yStart);
//								arealightSampler.shuffleSamples();
							}else if(tracer instanceof PathTracer || tracer instanceof HybridPathTracing){
//								int totalSamples = 0;
//								if(BRANCHING_FACTOR <= 1)
//									totalSamples = MAX_BOUNCES + 1;
//								else
//									totalSamples = (int)((Math.pow(BRANCHING_FACTOR,(MAX_BOUNCES+1))-1) / (BRANCHING_FACTOR-1))-1;
								
//								System.out.println(SAMPLES_PER_PIXEL * totalSamples * tile.getWidth() * tile.getHeight());
								materialSampler = new PureRandom(tile.xStart + tile.getWidth() * tile.yStart);
//								materialSampler = new PureRandom(SAMPLES_PER_PIXEL * totalSamples, tile.getWidth() * tile.getHeight(), tile.xStart + tile.getWidth() * tile.yStart);
//								materialSampler.mapSamplesToCosineHemisphere();
								if(tracer instanceof HybridPathTracing){
									arealightSampler = new PureRandom(tile.xStart + tile.getWidth() * tile.yStart);
//									arealightSampler = new PureRandom(SAMPLES_PER_PIXEL * totalSamples, tile.getWidth() * tile.getHeight(), tile.xStart + tile.getWidth() * tile.yStart);
								}
							}
						}
						// iterate over the contents of the tile
						for (int y = tile.yStart; y < tile.yEnd; ++y) {
							for (int x = tile.xStart; x < tile.xEnd; ++x) {
								RGBColor color = new RGBColor();
								Sample[] samples = pixelSampler.generateSamples(SAMPLES_PER_PIXEL);
								for (int i = 0; i < SAMPLES_PER_PIXEL; i++) {
									// get sample of sampler
//									Sample sample = pixelSampler.getSampleUnitSquare();
									Sample sample = samples[i];
									sample.x += x;
									sample.y += y;
									// create a ray through the center of the
									// pixel.
									Ray ray = camera.generateRay(sample);

									// add to totalcolor
									RGBColor.add(tracer.traceRay(ray, arealightSampler, materialSampler, 0), color);
								}

								// get average of the samples
								RGBColor.scale(1.0 / SAMPLES_PER_PIXEL, color);

								if (OUT_OF_GAMUT)
									maxToOne(color);
								else
									clampToColor(color);

								// add a color contribution to the pixel
								buffer.getPixel(x, y).add(color.r, color.g, color.b);
							}
						}
						
						// update the graphical user interface
						if (panel != null)
							panel.update(tile);

						// update the progress reporter
						reporter.update(tile.getWidth() * tile.getHeight());
					} catch (Exception e) {
						e.printStackTrace();
					}
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
	}

	public void renderFalseColor() {
		/**********************************************************************
		 * Multi-threaded rendering of the scene
		 *********************************************************************/
		final int[][] totalIntersections = new int[buffer.xResolution][buffer.yResolution];

		final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
					// iterate over the contents of the tile
					for (int y = tile.yStart; y < tile.yEnd; ++y) {
						for (int x = tile.xStart; x < tile.xEnd; ++x) {
							// create a ray through the center of the
							// pixel.
							Ray ray = camera.generateRay(new Sample(x + 0.5, y + 0.5));
							totalIntersections[x][y] = hitObjects(ray).totalIntersections;
						}
					}
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

		int max = 0;
		for (int y = 0; y < buffer.yResolution; y++) {
			for (int x = 0; x < buffer.xResolution; x++) {
				if (totalIntersections[x][y] > max)
					max = totalIntersections[x][y];
			}
		}

		for (int y = 0; y < buffer.yResolution; y++) {
			for (int x = 0; x < buffer.xResolution; x++) {
				RGBColor falseColor = new RGBColor(falseColor1);
				RGBColor.add(falseColor2.scale(totalIntersections[x][y] / (double) max), falseColor);
				// add a color contribution to the pixel
				buffer.getPixel(x, y).add(falseColor.r, falseColor.g, falseColor.b);
			}
		}

		if (panel != null)
			panel.update(new Tile(buffer, 0, 0, buffer.xResolution, buffer.yResolution));

		// signal the reporter that the task is done
		reporter.done();
	}

	public void addObject(GeometricObject object) {
		shapes.add(object);
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public ShadeRec hitObjects(Ray ray) {
		ShadeRec shadeRec = new ShadeRec(this);
		Vector3d normal = new Vector3d();
		Point3d localHitPoint = new Point3d();
		Vector2d textureCoords = new Vector2d();
		GeometricObject objectHit = null;
		double tmin = shadeRec.t;
		for (GeometricObject object : shapes) {
			if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
				shadeRec.isHit = true;
				tmin = shadeRec.t;
				objectHit = shadeRec.object;
				shadeRec.hitPoint = ray.origin.add(ray.direction.scale(tmin));
				normal.set(shadeRec.normal.x, shadeRec.normal.y, shadeRec.normal.z);
				localHitPoint.set(shadeRec.localHitPoint.x, shadeRec.localHitPoint.y, shadeRec.localHitPoint.z);
				textureCoords.set(shadeRec.textureCoords.x, shadeRec.textureCoords.y);
			}
		}
		if (shadeRec.isHit) {
			shadeRec.t = tmin;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = localHitPoint;
			shadeRec.textureCoords = textureCoords;
			shadeRec.object = objectHit;
		}
		return shadeRec;
	}

	public void exportResult(double sensitivity, double gamma) {
		BufferedImage result = buffer.toBufferedImage(sensitivity, gamma);
		try {
			ImageIO.write(result, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// out of gamut check
	private void maxToOne(RGBColor color) {
		double maxValue = Math.max(color.r, Math.max(color.g, color.b));
		if (maxValue > 1.0) {
			color = color.scale(1 / maxValue);
		}
	}

	// out of gamut
	private void clampToColor(RGBColor color) {
		if (color.r > 1.0 || color.g > 1.0 || color.b > 1.0) {
			color.r = 1;
			color.g = 0;
			color.b = 0;
		}
	}
}
