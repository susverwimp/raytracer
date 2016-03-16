package tracer;

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
import light.Ambient;
import light.Light;
import light.PointLight;
import loader.ModelData;
import loader.OBJFileLoader;
import mapping.SphericalMap;
import material.SVMatte;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Transformation;
import math.Vector3d;
import sampling.Sample;
import shape.GeometricObject;
import shape.Instance;
import shape.Plane;
import shape.Sphere;
import shape.trianglemesh.SmoothUVMeshTriangle;
import texture.Checker3D;
import texture.ImageTexture;
import util.ShadeRec;

public class World {

	private final Camera camera;
	private final FrameBuffer buffer;
	private final ProgressReporter reporter;
	private final ImagePanel panel;

	private Tracer tracer;
	public RGBColor backgroundColor;
	public Light ambient;
	public final List<GeometricObject> shapes = new ArrayList<>();
	public final List<Light> lights = new ArrayList<>();

	private static final boolean OUT_OF_GAMUT = true;

	public World(Camera camera, FrameBuffer buffer, ProgressReporter reporter, ImagePanel panel) {
		this.camera = camera;
		this.buffer = buffer;
		this.reporter = reporter;
		this.panel = panel;
		this.tracer = new RayCast(this);
		this.backgroundColor = new RGBColor();
		this.ambient = new Ambient();
		build();
	}

	private void build() {
		/**********************************************************************
		 * Initialize the scene
		 *********************************************************************/

		Transformation worldSphereTransformation = Transformation.translate(2, 0, 6).append(Transformation.rotateY(-45));
		Transformation houseTransformation = Transformation.translate(0, -1, 1.5).append(Transformation.rotateY(45));

		// create a world sphere
		try {
			Sphere sphere = new Sphere(null);
			BufferedImage image = null;
			image = ImageIO.read(new File("res/textures/world_texture.png"));
			SVMatte imageMatte = new SVMatte(
					new ImageTexture(image.getWidth(), image.getHeight(), image, new SphericalMap()));
			imageMatte.setKA(0);
			imageMatte.setKD(0.7);
			shapes.add(new Instance(sphere, true, worldSphereTransformation, imageMatte));
		} catch (IOException e) {
			e.printStackTrace();
		}

		 //create a plane with black-white checkertexture
		 SVMatte checkerMatte = new SVMatte(new Checker3D(3, new RGBColor(),
		 new RGBColor(1,1,1)));
		 checkerMatte.setKA(0);
		 checkerMatte.setKD(0.7);
		 shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0),
		 checkerMatte));

		// create house object
		try {
			BufferedImage image = ImageIO.read(new File("res/textures/house_texture.jpg"));
			SVMatte imageMatte = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
			imageMatte.setKA(0);
			imageMatte.setKD(0.7);
			ModelData data = OBJFileLoader.loadOBJ("res/models/house.obj");
			int[] indices = data.getIndices();
			for (int i = 0; i < indices.length; i += 3) {
				shapes.add(new Instance(new SmoothUVMeshTriangle(data.mesh, indices[i], indices[i + 1], indices[i + 2], imageMatte), true, houseTransformation, null));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		lights.add(new PointLight(1000, new RGBColor(1, 1, 1), new Point3d(0, 7, -5)));
	}

	public void renderScene() {
		/**********************************************************************
		 * Multi-threaded rendering of the scene
		 *********************************************************************/

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
					try {
						// iterate over the contents of the tile
						for (int y = tile.yStart; y < tile.yEnd; ++y) {
							for (int x = tile.xStart; x < tile.xEnd; ++x) {
								// create a ray through the center of the
								// pixel.
								Ray ray = camera.generateRay(new Sample(x + 0.5, y + 0.5));

								// get color to draw
								RGBColor color = tracer.traceRay(ray);
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
		double tmin = Double.POSITIVE_INFINITY;
		for (GeometricObject object : shapes) {
			if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
				shadeRec.isHit = true;
				tmin = shadeRec.t;
				shadeRec.material = object.material;
				shadeRec.hitPoint = ray.origin.add(ray.direction.scale(tmin));
				normal = shadeRec.normal;
				localHitPoint = shadeRec.localHitPoint;
			}
		}
		if (shadeRec.isHit) {
			shadeRec.t = tmin;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = localHitPoint;
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
