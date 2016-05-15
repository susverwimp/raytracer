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
import light.AreaLight;
import light.Light;
import light.PointLight;
import loader.OBJFileLoader;
import mapping.SphericalMap;
import material.Emissive;
import material.SVMatte;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Transformation;
import math.Vector2d;
import math.Vector3d;
import sampling.Jittered;
import sampling.PureRandom;
import sampling.Regular;
import sampling.Sample;
import sampling.Sampler;
import shape.BoundingVolume;
import shape.GeometricObject;
import shape.Instance;
import shape.Plane;
import shape.Rectangle;
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

	public Tracer tracer;
	public static final int MAX_DEPTH = Integer.MAX_VALUE;
	public static final int SAMPLES_PER_PIXEL = 16;
	public static final int BRANCHING_FACTOR = 1;
	public static final RGBColor BACKGROUND_COLOR = new RGBColor();
	public Light ambient = new Ambient();
	public final List<GeometricObject> shapes = new ArrayList<>();
	public final List<Light> lights = new ArrayList<>();

	private static final RGBColor falseColor1 = new RGBColor(0, 0, 0);
	private static final RGBColor falseColor2 = new RGBColor(1, 1, 1);

	private static final boolean useBoundingVolumeHierarchy = true;
	private static final boolean OUT_OF_GAMUT = true;

	public World(Camera camera, FrameBuffer buffer, ProgressReporter reporter, ImagePanel panel) throws IOException {
		this.camera = camera;
		this.buffer = buffer;
		this.reporter = reporter;
		this.panel = panel;
		this.tracer = new PathTracer(this);
		build();
	}

	private void build() throws IOException {
		/**********************************************************************
		 * Initialize the scene
		 *********************************************************************/

		Transformation worldSphereTransformation = Transformation.translate(1.5, 0, -4);
		Transformation houseTransformation = Transformation.translate(0, -1, -2).append(Transformation.rotateY(90));
		Transformation appleTransformation = Transformation.translate(0, -1, -2).append(Transformation.rotateX(90));
		Transformation buddhaTransformation = Transformation.translate(0, 0, -2).append(Transformation.scale(2, 2, 2)).append(Transformation.rotateY(180));
		Transformation bunnyTransformation = Transformation.translate(0, -0.5, -1.2).append(Transformation.scale(0.1, 0.1, 0.1));

		List<GeometricObject> bvhs = new ArrayList<>();
		BoundingVolume bvh = null;
		BufferedImage image = null;
		Mesh mesh = null;

		// create a world sphere
//		bvh = new BoundingVolume();
//		image = ImageIO.read(new File("res/textures/world_texture.jpg"));
//		SVMatte imageMatteWorld = new SVMatte(
//				new ImageTexture(image.getWidth(), image.getHeight(), image, new SphericalMap()));
//		imageMatteWorld.setKA(0);
//		imageMatteWorld.setKD(0.7);
//		Sphere sphere = new Sphere(imageMatteWorld);
//		if (useBoundingVolumeHierarchy) {
//			bvh.addObject(sphere);
//			bvh.calculateHierarchy();
//			bvhs.add(new Instance(bvh, true, worldSphereTransformation, null));
//		} else {
//			shapes.add(new Instance(sphere, true, worldSphereTransformation, imageMatteWorld));
//		}

		// create house object
//		bvh = new BoundingVolume();
//		image = ImageIO.read(new File("res/textures/house_texture.jpg"));
//		SVMatte imageMatteHouse = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
//		imageMatteHouse.setKA(0.05);
//		imageMatteHouse.setKD(0.7);
//		mesh = OBJFileLoader.loadOBJ("res/models/house.obj");
//		for (int i = 0; i < mesh.indices.length; i += 3) {
//			if (useBoundingVolumeHierarchy) {
//				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
//						imageMatteHouse));
//			} else {
//				shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
//						mesh.indices[i + 2], imageMatteHouse), true, houseTransformation, null));
//			}
//		}
//		bvh.calculateHierarchy();
//		bvhs.add(new Instance(bvh, true, houseTransformation, null));

		// create apple object with bvh
//		bvh = new BoundingVolume();
//		image = ImageIO.read(new File("res/textures/apple_texture.jpg"));
//		SVMatte imageMatteApple = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
//		imageMatteApple.setKA(0);
//		imageMatteApple.setKD(0.7);
//		mesh = OBJFileLoader.loadOBJ("res/models/apple.obj");
//		for (int i = 0; i < mesh.indices.length; i += 3) {
//			if (useBoundingVolumeHierarchy) {
//				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
//						imageMatteApple));
//			} else {
//				shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
//						mesh.indices[i + 2], imageMatteApple), true, appleTransformation, null));
//			}
//		}
//		bvh.calculateHierarchy();
//		bvhs.add(new Instance(bvh, true, appleTransformation, null));

		// create bunny
		bvh = new BoundingVolume();
		SVMatte constantMatte = new SVMatte(new ConstantColor(new RGBColor(0.5, 0.5, 0.5)));
		constantMatte.setKA(0);
		constantMatte.setKD(0.7);
		mesh = OBJFileLoader.loadOBJ("res/models/bunny.obj");
		for (int i = 0; i < mesh.indices.length; i += 3) {
			if (useBoundingVolumeHierarchy) {
				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
						constantMatte));
			} else {
				shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
						mesh.indices[i + 2], constantMatte), true, bunnyTransformation, null));
			}
		}
		bvh.calculateHierarchy();
		bvhs.add(new Instance(bvh, true, bunnyTransformation, null));

		// create buddha object
//		bvh = new BoundingVolume();
//		mesh = OBJFileLoader.loadOBJ("res/models/buddha.obj");
//		for (int i = 0; i < mesh.indices.length; i += 3) {
//			if (useBoundingVolumeHierarchy) {
//				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
//						constantMatte));
//			} else {
//				shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
//						mesh.indices[i + 2], constantMatte), true, buddhaTransformation, null));
//			}
//		}
//		bvh.calculateHierarchy();
//		bvhs.add(new Instance(bvh, true, buddhaTransformation, null));

		if (useBoundingVolumeHierarchy) {
			for (GeometricObject object : bvhs) {
				shapes.add(object);
			}
		}

//		SVMatte checkerMatte = new SVMatte(new Checker3D(1, new RGBColor(), new RGBColor(1, 1, 1)));
//		checkerMatte.setKA(0);
//		checkerMatte.setKD(0.7);
//		Disk disk = new Disk(new Point3d(0,-1, -5), new Vector3d(0, 1, 0), 1, checkerMatte);
//		shapes.add(disk);
		
		Emissive emissive = new Emissive();
		emissive.scaleRadiance(10.0);
		emissive.setCE(1, 1, 1);
		
		
		
		// create a plane with black-white checkertexture
//		SVMatte checkerMatte = new SVMatte(new Checker3D(1, new RGBColor(), new RGBColor(1, 1, 1)));
//		checkerMatte.setKA(0);
//		checkerMatte.setKD(0.7);
//		shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), checkerMatte));

		//create house with light
		SVMatte leftWallColor = new SVMatte(new ConstantColor(new RGBColor(1,0,0)));
		leftWallColor.setKA(0.0);
		leftWallColor.setKD(0.7);
		Rectangle leftWall = new Rectangle(new Point3d(-1,-1,0), new Vector3d(0,0,-2), new Vector3d(0,2,0), new Vector3d(1,0,0), leftWallColor);
		shapes.add(leftWall);
		
		SVMatte rightWallColor = new SVMatte(new ConstantColor(new RGBColor(0,1,0)));
		rightWallColor.setKA(0.0);
		rightWallColor.setKD(0.7);
		Rectangle rightWall = new Rectangle(new Point3d(1,-1,0), new Vector3d(0,0,-2), new Vector3d(0,2,0), new Vector3d(-1,0,0), rightWallColor);
		shapes.add(rightWall);
		
		SVMatte backWallColor = new SVMatte(new ConstantColor(new RGBColor(0.9,0.7,0.8)));
		backWallColor.setKA(0.0);
		backWallColor.setKD(0.7);
		Rectangle backWall = new Rectangle(new Point3d(-1,-1,-2), new Vector3d(0,2,0), new Vector3d(2,0,0), new Vector3d(0,0,1), backWallColor);
		shapes.add(backWall);
		
		SVMatte floorColor = new SVMatte(new ConstantColor(new RGBColor(0.9,0.7,0.8)));
		floorColor.setKA(0.0);
		floorColor.setKD(0.7);
		Rectangle floor = new Rectangle(new Point3d(-1,-1,0), new Vector3d(0,0,-2), new Vector3d(2,0,0), new Vector3d(0,1,0), floorColor);
		shapes.add(floor);
		
		SVMatte ceilingColor = new SVMatte(new ConstantColor(new RGBColor(0.9,0.7,0.8)));
		ceilingColor.setKA(0.0);
		ceilingColor.setKD(0.7);
		Rectangle ceiling = new Rectangle(new Point3d(-1,1,0), new Vector3d(0,0,-2), new Vector3d(2,0,0), new Vector3d(0,-1,0), ceilingColor);
		shapes.add(ceiling);
		
		
		
		//create rectangle
		Rectangle lightRectangle = new Rectangle(new Point3d(-0.1,0.999,-1.5), new Vector3d(0,0,0.2), new Vector3d(0.2,0,0), new Vector3d(0,-1,0), emissive);
		lightRectangle.setShadows(true);
		shapes.add(lightRectangle);
		
		Light arealight = new AreaLight(lightRectangle);
		arealight.setShadows(true);
		lights.add(arealight);
//		lights.add(new PointLight(100, new RGBColor(1, 1, 1), new Point3d(1, 1, 0)));
		// lights.add(new PointLight(100, new RGBColor(1,0.1,0.1), new
		// Point3d(-1, 2, -2)));
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
						int seed = tile.xStart + tile.getWidth() * tile.yStart;
						Sampler pixelSampler;
						Sampler arealightSampler;
						if(SAMPLES_PER_PIXEL == 1){
							pixelSampler = new Regular();
							arealightSampler = new Regular();
						}else{
							pixelSampler = new Jittered(SAMPLES_PER_PIXEL, (tile.yEnd - tile.yStart) * (tile.xEnd - tile.xStart),  tile.xStart + tile.getWidth() * tile.yStart);
							arealightSampler = new Jittered(SAMPLES_PER_PIXEL, ((tile.yEnd - tile.yStart) * (tile.xEnd - tile.xStart)) * lights.size(), tile.xStart + tile.getWidth() * tile.yStart);
							arealightSampler.shuffleSamples();
						}
						// iterate over the contents of the tile
						for (int y = tile.yStart; y < tile.yEnd; ++y) {
							for (int x = tile.xStart; x < tile.xEnd; ++x) {
								RGBColor color = new RGBColor();

								for (int i = 0; i < SAMPLES_PER_PIXEL; i++) {
									// get sample of sampler
									Sample sample = pixelSampler.getSampleUnitSquare();
									sample.x += x;
									sample.y += y;
									// create a ray through the center of the
									// pixel.
									Ray ray = camera.generateRay(sample);

									// add to totalcolor
									RGBColor.add(tracer.traceRay(ray, arealightSampler, 0, seed), color);
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
