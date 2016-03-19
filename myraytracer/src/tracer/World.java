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
import loader.OBJFileLoader;
import mapping.SphericalMap;
import material.SVMatte;
import material.SVPhong;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Transformation;
import math.Vector2d;
import math.Vector3d;
import sampling.Sample;
import shape.BoundingVolume;
import shape.GeometricObject;
import shape.Instance;
import shape.Plane;
import shape.Sphere;
import shape.trianglemesh.Mesh;
import shape.trianglemesh.SmoothUVMeshTriangle;
import texture.Checker3D;
import texture.ConstantColor;
import texture.ImageTexture;
import util.ShadeRec;

public class World {

	private final Camera camera;
	private final FrameBuffer buffer;
	private final ProgressReporter reporter;
	private final ImagePanel panel;

	private Tracer rayCastTracer;
	public RGBColor backgroundColor;
	public Light ambient;
	public final List<GeometricObject> shapes = new ArrayList<>();
	public final List<Light> lights = new ArrayList<>();

	private static final RGBColor falseColor1 = new RGBColor(0, 0, 1);
	private static final RGBColor falseColor2 = new RGBColor(0, 1, 0);

	private static final boolean OUT_OF_GAMUT = true;

	public World(Camera camera, FrameBuffer buffer, ProgressReporter reporter, ImagePanel panel) {
		this.camera = camera;
		this.buffer = buffer;
		this.reporter = reporter;
		this.panel = panel;
		this.rayCastTracer = new RayCast(this);
		this.backgroundColor = new RGBColor();
		this.ambient = new Ambient();
		build();
	}

	private void build() {
		/**********************************************************************
		 * Initialize the scene
		 *********************************************************************/

		Transformation worldSphereTransformation = Transformation.translate(-1, 0, -6);
		Transformation houseTransformation = Transformation.translate(0, -1, -2).append(Transformation.rotateY(-225));
		Transformation appleTransformation = Transformation.translate(0, -1, -2).append(Transformation.rotateX(90));

		// create a world sphere
//		try {
//			Sphere sphere = new Sphere(null);
//			BufferedImage image = null;
//			image = ImageIO.read(new File("res/textures/world_texture.jpg"));
//			SVMatte imageMatte = new SVMatte(
//					new ImageTexture(image.getWidth(), image.getHeight(), image, new SphericalMap()));
//			imageMatte.setKA(0);
//			imageMatte.setKD(0.7);
//
//			SVPhong imagePhong = new SVPhong(
//					new ImageTexture(image.getWidth(), image.getHeight(), image, new SphericalMap()));
//			imagePhong.setKA(0);
//			imagePhong.setKD(0.7);
//			imagePhong.setExp(10);
//			imagePhong.setKS(1);
//			shapes.add(new Instance(sphere, true, worldSphereTransformation, imagePhong));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// create a plane with black-white checkertexture
		SVMatte checkerMatte = new SVMatte(new Checker3D(3, new RGBColor(), new RGBColor(1, 1, 1)));
		checkerMatte.setKA(0);
		checkerMatte.setKD(0.7);
		
		SVPhong checkerPhong = new SVPhong(new Checker3D(3, new RGBColor(), new RGBColor(1, 1, 1)));
		checkerPhong.setKA(0);
		checkerPhong.setKD(0.7);
		checkerPhong.setExp(27);
		checkerPhong.setKS(0.5);
		shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), checkerMatte));

		// create house object
//		try {
//			BufferedImage image = ImageIO.read(new File("res/textures/house_texture.jpg"));
//			SVMatte imageMatte = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
//			imageMatte.setKA(0);
//			imageMatte.setKD(0.7);
//			Mesh mesh = OBJFileLoader.loadOBJ("res/models/house.obj");
//			SVMatte colorMatte = new SVMatte(new ConstantColor(new RGBColor(0.5, 0.5, 0.5)));
//			colorMatte.setKA(0);
//			colorMatte.setKD(0.7);
//			for (int i = 0; i < mesh.indices.length; i += 3) {
//				shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
//						mesh.indices[i + 2], imageMatte), true, houseTransformation, null));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// create apple object
		try {
			BoundingVolume bvh = new BoundingVolume();
			BufferedImage image = ImageIO.read(new File("res/textures/apple_texture.jpg"));
			SVMatte imageMatte = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
			imageMatte.setKA(0);
			imageMatte.setKD(0.7);
			Mesh mesh = OBJFileLoader.loadOBJ("res/models/apple.obj");
			SVMatte colorMatte = new SVMatte(new ConstantColor(new RGBColor(0.5, 0.5, 0.5)));
			colorMatte.setKA(0);
			colorMatte.setKD(0.7);

			SVPhong imagePhong = new SVPhong(
					new ImageTexture(image.getWidth(), image.getHeight(), image, new SphericalMap()));
			imagePhong.setKA(0);
			imagePhong.setKD(0.7);
			imagePhong.setExp(27);
			imagePhong.setKS(0.5);
			for (int i = 0; i < mesh.indices.length; i += 3) {
				bvh.addObject(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
						mesh.indices[i + 2], imageMatte), true, appleTransformation, null));
			}
			bvh.calculateHierarchy();
			shapes.add(bvh);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lights.add(new PointLight(100, new RGBColor(1, 1, 1), new Point3d(1, 1, 0)));
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
								RGBColor color = rayCastTracer.traceRay(ray);
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
		double tmin = Double.POSITIVE_INFINITY;
		for (GeometricObject object : shapes) {
			if (object.intersect(ray, shadeRec) && shadeRec.t < tmin) {
				shadeRec.isHit = true;
				tmin = shadeRec.t;
				shadeRec.material = object.material;
				shadeRec.hitPoint = ray.origin.add(ray.direction.scale(tmin));
				normal.set(shadeRec.normal.x, shadeRec.normal.y, shadeRec.normal.z);
				localHitPoint.set(shadeRec.localHitPoint.x, shadeRec.localHitPoint.y, shadeRec.localHitPoint.z);
				textureCoords.setX(shadeRec.textureCoords.x);
				textureCoords.setY(shadeRec.textureCoords.y);
			}
		}
		if (shadeRec.isHit) {
			shadeRec.t = tmin;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = localHitPoint;
			shadeRec.textureCoords = textureCoords;
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
