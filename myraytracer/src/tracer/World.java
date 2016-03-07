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
import material.Matte;
import math.Point3d;
import math.RGBColor;
import math.Ray;
import math.Transformation;
import math.Vector3d;
import sampling.Sample;
import shape.GeometricObject;
import shape.Plane;
import shape.Sphere;
import shape.Triangle;
import shape.trianglemesh.FlatMeshTriangle;
import shape.trianglemesh.Mesh;
import shape.trianglemesh.SmoothMeshTriangle;
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
	
	public World(Camera camera, FrameBuffer buffer, ProgressReporter reporter, ImagePanel panel){
		this.camera = camera;
		this.buffer = buffer;
		this.reporter = reporter;
		this.panel = panel;
		this.tracer = new RayCast(this);
		this.backgroundColor = new RGBColor();
		this.ambient = new Ambient();
		build();
	}
	
	private void build(){
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

		Matte matte = new Matte();
		matte.setCD(new RGBColor(0.5, 0.5, 0.5));
		matte.setKA(0.05);
		matte.setKD(0.7);
		
//		shapes.add(new Sphere(t1, matte));
//		shapes.add(new Sphere(t2, matte));
//		shapes.add(new Sphere(t3, matte));
//		shapes.add(new Sphere(t4, matte));
//		shapes.add(new Sphere(t5, matte));
		shapes.add(new Plane(new Point3d(0, -8, 0), new Vector3d(0, 1, 0), matte));
//		shapes.add(new Triangle(new Point3d(-1, 2, 4), new Point3d(0, -2, 4), new Point3d(1, 2, 4), matte));
//		shapes.add(new Triangle(new Point3d(-1, 2, 4), new Point3d(1, 2, 4), new Point3d(0, -2, 4), matte));
		
		ModelData data = OBJFileLoader.loadOBJ("res/bunny.obj");
		
		for(int i = 0; i < data.getIndices().length; i+=3){
			shapes.add(new SmoothMeshTriangle(data.mesh, data.getIndices()[i], data.getIndices()[i+1], data.getIndices()[i+2], matte));
		}
		
		lights.add(new PointLight(0.7, new RGBColor(1, 1, 1), new Point3d(0, 15, 0)));
	}
	
	public void renderScene(){
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
					try{
						// iterate over the contents of the tile
						for (int y = tile.yStart; y < tile.yEnd; ++y) {
							for (int x = tile.xStart; x < tile.xEnd; ++x) {
								// create a ray through the center of the
								// pixel.
								Ray ray = camera.generateRay(new Sample(x + 0.5,
										y + 0.5));
	
								// get color to draw
								RGBColor color = tracer.traceRay(ray);
								if(OUT_OF_GAMUT)
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
					}catch(Exception e){
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
	
	public void addObject(GeometricObject object){
		shapes.add(object);
	}
	
	public void addLight(Light light){
		lights.add(light);
	}
	
	public ShadeRec hitObjects(Ray ray){
		ShadeRec shadeRec = new ShadeRec(this);
		Vector3d normal = new Vector3d();
		Point3d localHitPoint = new Point3d();
		double tmin = Double.POSITIVE_INFINITY;
		for(GeometricObject object : shapes){
			if(object.intersect(ray, shadeRec) && shadeRec.t < tmin){
				shadeRec.isHit = true;
				tmin = shadeRec.t;
				shadeRec.material = object.material;
				shadeRec.hitPoint = ray.origin.add(ray.direction.scale(tmin));
				normal = shadeRec.normal;
				localHitPoint = shadeRec.localHitPoint;
			}
		}
		if(shadeRec.isHit){
			shadeRec.t = tmin;
			shadeRec.normal = normal;
			shadeRec.localHitPoint = localHitPoint;
		}
		return shadeRec;
	}
	
	public void exportResult(double sensitivity, double gamma){
		BufferedImage result = buffer.toBufferedImage(sensitivity, gamma);
		try {
			ImageIO.write(result, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//out of gamut check
	private void maxToOne(RGBColor color){
		double maxValue = Math.max(color.r, Math.max(color.g, color.b));
		if(maxValue > 1.0){
			color = color.scale(1/maxValue);
		}
	}
	
	//out of gamut 
	private void clampToColor(RGBColor color){
		if(color.r > 1.0 || color.g > 1.0 || color.b > 1.0){
			color.r = 1;
			color.g = 0;
			color.b = 0;
		}
	}
}
