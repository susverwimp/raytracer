package world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import camera.PerspectiveCamera;
import light.AreaLight;
import light.Light;
import loader.OBJFileLoader;
import material.Emissive;
import material.Material;
import material.SVReflective;
import material.SVMatte;
import math.Point3d;
import math.RGBColor;
import math.Transformation;
import math.Vector3d;
import shape.BoundingVolume;
import shape.Instance;
import shape.Plane;
import shape.Rectangle;
import shape.Sphere;
import shape.trianglemesh.Mesh;
import shape.trianglemesh.SmoothUVMeshTriangle;
import texture.Checker3D;
import texture.ConstantColor;
import texture.ImageTexture;
import tracer.AreaLighting;
import tracer.HybridPathTracing;
import tracer.PathTracer;

public class WorldBuilder {

	public static final String CORNELL_BOX_REFLECTIVE_HYBRID_PATH_TRACING = "cornellbox-reflective-hybridpathtracing";
	public static final String CORNELL_BOX_REFLECTIVE_PATH_TRACING = "cornellbox-reflective-pathtracing";
	public static final String CORNELL_BOX_OTHER_VIEW_PATH_TRACING = "cornellbox-other-view-pathtracing";
	public static final String CORNELL_BOX_HYBRID_PATH_TRACING = "cornellbox-hybridpathtracing";
	public static final String CORNELL_BOX_PATH_TRACING = "cornellbox-pathtracing";
	public static final String CORNELL_BOX_AREALIGHT_TRACING = "cornellbox-arealighting";
	public static final String CAUSTICS_PATH_TRACING = "caustics_pathtracing";
	public static final String CAUSTICS_HYBRID_PATH_TRACING = "caustics_hybridpathtracing";
	public static final String CAUSTICS_HYBRID_PATH_TRACING2 = "caustics_hybridpathtracing2";
	public static final String APPLE_WITH_FLOOR_WITH_BHV = "apple_with_floor_with_bhv";
	public static final String APPLE_WITH_FLOOR_WITHOUT_BHV = "apple_with_floor_without_bhv";

	public static void build(String scene, int width, int height, boolean big, World world) {

		/**********************************************************************
		 * Initialize the scene
		 *********************************************************************/
		if (scene.equals(CORNELL_BOX_REFLECTIVE_HYBRID_PATH_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation bigSphereTransformation = Transformation.translate(-0.4, -0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));
			SVReflective bigSphereReflection = new SVReflective(new ConstantColor(new RGBColor(1, 1, 1)));
			bigSphereReflection.setKR(1);
			world.shapes.add(
					new Instance(new Sphere(bigSphereReflection), true, bigSphereTransformation, bigSphereReflection));

			Transformation bigSphere2Transformation = Transformation.translate(0.4, -0.5, -1.6)
					.append(Transformation.scale(0.5, 0.5, 0.5)).append(Transformation.scale(0.4, 0.4, 0.4));
			bigSphereReflection.setKR(1);
			world.shapes.add(
					new Instance(new Sphere(bigSphereReflection), true, bigSphere2Transformation, bigSphereReflection));

			createCornellBoxLight(1, big, world);

			world.tracer = new HybridPathTracing(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(1, 0, 0), new Point3d(-0.4, -0.5, -1.6),
					new Vector3d(0, 1, 0), 60);

			World.name = "hybrid_cornell_reflective_";

		} else if (scene.equals(CORNELL_BOX_REFLECTIVE_PATH_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation bigSphereTransformation = Transformation.translate(-0.4, -0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));
			SVReflective bigSphereReflection = new SVReflective(new ConstantColor(new RGBColor(1, 1, 1)));
			bigSphereReflection.setKR(1);
			world.shapes.add(
					new Instance(new Sphere(bigSphereReflection), true, bigSphereTransformation, bigSphereReflection));

			Transformation bigSphere2Transformation = Transformation.translate(0.4, -0.5, -1.6)
					.append(Transformation.scale(0.5, 0.5, 0.5)).append(Transformation.scale(0.4, 0.4, 0.4));
			bigSphereReflection.setKR(1);
			world.shapes.add(
					new Instance(new Sphere(bigSphereReflection), true, bigSphere2Transformation, bigSphereReflection));

			createCornellBoxLight(1, big, world);

			world.tracer = new PathTracer(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(1, 0, 0), new Point3d(-0.4, -0.5, -1.6),
					new Vector3d(0, 1, 0), 60);

			World.name = "path_cornell_reflective_";

		} else if (scene.equals(CORNELL_BOX_OTHER_VIEW_PATH_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation bigSphereTransformation = Transformation.translate(-0.4, -0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));
			SVMatte bigSphereColor = new SVMatte(new ConstantColor(new RGBColor(0.3, 0.8, 0.4)));
			bigSphereColor.setKD(0.7);
			world.shapes.add(new Instance(new Sphere(bigSphereColor), true, bigSphereTransformation, bigSphereColor));

			Transformation bigSphere2Transformation = Transformation.translate(0.4, -0.5, -1.6)
					.append(Transformation.scale(0.5, 0.5, 0.5)).append(Transformation.scale(0.4, 0.4, 0.4));
			SVMatte bigSphereColor2 = new SVMatte(new ConstantColor(new RGBColor(0.3, 0.6, 0.8)));
			bigSphereColor2.setKD(0.7);
			world.shapes
					.add(new Instance(new Sphere(bigSphereColor2), true, bigSphere2Transformation, bigSphereColor2));

			createCornellBoxLight(1, big, world);

			world.tracer = new PathTracer(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(1, 0, 0), new Point3d(-0.4, -0.5, -1.6),
					new Vector3d(0, 1, 0), 60);
			World.name = "path_cornell_otherview_";

		} else if (scene.equals(CORNELL_BOX_HYBRID_PATH_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation smallSphereTransformation = Transformation.translate(-0.7, -0.8, -1.3)
					.append(Transformation.scale(0.2, 0.2, 0.2));
			SVMatte smallSphereColor = new SVMatte(new ConstantColor(new RGBColor(0.1, 0.7, 0.1)));
			smallSphereColor.setKA(0.0);
			smallSphereColor.setKD(0.7);
			world.shapes
					.add(new Instance(new Sphere(smallSphereColor), true, smallSphereTransformation, smallSphereColor));

			Transformation bigSphereTransformation = Transformation.translate(0.4, 0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));
			SVMatte bigSphereColor = new SVMatte(new ConstantColor(new RGBColor(1, 1, 1)));
			bigSphereColor.setKA(0.0);
			bigSphereColor.setKD(0.7);
			world.shapes.add(new Instance(new Sphere(bigSphereColor), true, bigSphereTransformation, bigSphereColor));

			createCornellBoxLight(1, big, world);

			world.tracer = new HybridPathTracing(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(0, 0, 0), new Point3d(0, 0, -1),
					new Vector3d(0, 1, 0), 90);
			World.name = "hybrid_cornell_";

		} else if (scene.equals(CORNELL_BOX_PATH_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation smallSphereTransformation = Transformation.translate(-0.7, -0.8, -1.3)
					.append(Transformation.scale(0.2, 0.2, 0.2));
			SVMatte smallSphereColor = new SVMatte(new ConstantColor(new RGBColor(0.1, 0.7, 0.1)));
			smallSphereColor.setKA(0.0);
			smallSphereColor.setKD(0.7);
			world.shapes
					.add(new Instance(new Sphere(smallSphereColor), true, smallSphereTransformation, smallSphereColor));

			Transformation bigSphereTransformation = Transformation.translate(0.4, 0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));

			// SVReflective bigSphereReflection = new SVReflective(new
			// ConstantColor(new RGBColor(1, 1, 1)));
			// bigSphereReflection.setKR(1);
			SVMatte bigSphereColor = new SVMatte(new ConstantColor(new RGBColor(1, 1, 1)));
			bigSphereColor.setKA(0.0);
			bigSphereColor.setKD(0.7);
			world.shapes.add(new Instance(new Sphere(bigSphereColor), true, bigSphereTransformation, bigSphereColor));

			createCornellBoxLight(1, big, world);

			world.tracer = new PathTracer(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(0, 0, 0), new Point3d(0, 0, -1),
					new Vector3d(0, 1, 0), 90);
			World.name = "path_cornell_";
		} else if (scene.equals(CORNELL_BOX_AREALIGHT_TRACING)) {
			createCornellBoxWithoutLight(width, height, world);

			Transformation smallSphereTransformation = Transformation.translate(-0.7, -0.8, -1.3)
					.append(Transformation.scale(0.2, 0.2, 0.2));
			SVMatte smallSphereColor = new SVMatte(new ConstantColor(new RGBColor(0.1, 0.7, 0.1)));
			smallSphereColor.setKA(0.0);
			smallSphereColor.setKD(0.7);
			world.shapes
					.add(new Instance(new Sphere(smallSphereColor), true, smallSphereTransformation, smallSphereColor));

			Transformation bigSphereTransformation = Transformation.translate(0.4, 0.5, -1.6)
					.append(Transformation.scale(0.4, 0.4, 0.4));
			SVMatte bigSphereColor = new SVMatte(new ConstantColor(new RGBColor(1, 1, 1)));
			bigSphereColor.setKA(0.0);
			bigSphereColor.setKD(0.7);
			world.shapes.add(new Instance(new Sphere(bigSphereColor), true, bigSphereTransformation, bigSphereColor));

			createCornellBoxLight(1, big, world);

			world.tracer = new AreaLighting(world);
			world.camera = new PerspectiveCamera(width, height, new Point3d(0, 0, 0), new Point3d(0, 0, -1),
					new Vector3d(0, 1, 0), 90);
			World.name = "area_cornell_";
		} else if (scene.equals(CAUSTICS_PATH_TRACING)) {
			Emissive emissive = new Emissive();
			emissive.setPower(20.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle = new Rectangle(new Point3d(0.3, 0.4, 1), new Vector3d(0, 0, 0.3),
					new Vector3d(0.3, -0.3, 0), new Vector3d(0, -1, -1), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			Light arealight = new AreaLight(lightRectangle);
			arealight.setShadows(true);
			world.lights.add(arealight);

			BoundingVolume bvh = new BoundingVolume();
//
//			// reflective ring
			SVReflective ringColor = new SVReflective(new ConstantColor(new RGBColor(0.7, 0.3, 0.2)));
			ringColor.setKR(1);
			Transformation ringTransformation = Transformation.translate(0, -0.9, -1)
					.append(Transformation.scale(1, 0.5, 1));
			Mesh mesh = OBJFileLoader.loadOBJ("res/models/ring.obj");
			for (int i = 0; i < mesh.indices.length; i += 3) {
				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
						ringColor));
			}
			bvh.calculateHierarchy();
			world.shapes.add(new Instance(bvh, true, ringTransformation, null));
			
			SVMatte floorColor = new SVMatte(new ConstantColor(new RGBColor(0.8, 0.8, 0.2)));
			floorColor.setKD(0.8);
			world.shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), floorColor));
			
			world.tracer = new PathTracer(world);

			world.camera = new PerspectiveCamera(width, height, new Point3d(-0.3, 0.3, 1.5),
					new Vector3d(0.1, -0.4, -1), new Vector3d(0, 1, 0), 70);

			World.name = "path_caustics_";
		} else if (scene.equals(CAUSTICS_HYBRID_PATH_TRACING)) {
			// create light
			Emissive emissive = new Emissive();
			emissive.setPower(20.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle = new Rectangle(new Point3d(0.3, 0.3, -1.5), new Vector3d(0, 0, 0.1),
					new Vector3d(0.1, -0.1, 0), new Vector3d(-1, -1, 0), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			Light arealight = new AreaLight(lightRectangle);
			arealight.setShadows(true);
			world.lights.add(arealight);

			BufferedImage image;
			try {
				Transformation floorTransformation = Transformation.translate(0, -1, -1.3);
				image = ImageIO.read(new File("res/textures/wood_texture.jpg"));
				SVMatte floorColor = new SVMatte(new ImageTexture(image.getWidth(), image.getHeight(), image, null));
				floorColor.setKA(0.0);
				floorColor.setKD(0.7);
				BoundingVolume bvh = new BoundingVolume();

				Mesh mesh = OBJFileLoader.loadOBJ("res/models/plane.obj");
				for (int i = 0; i < mesh.indices.length; i += 3) {
					bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
							mesh.indices[i + 2], floorColor));
				}
				bvh.calculateHierarchy();
				world.shapes.add(new Instance(bvh, true, floorTransformation, null));

				// Rectangle floor = new Rectangle(new Point3d(-1, -1, 0), new
				// Vector3d(0, 0, -2), new Vector3d(2, 0, 0),
				// new Vector3d(0, 1, 0), floorColor);
				// world.shapes.add(floor);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// reflective ring
			SVReflective ringColor = new SVReflective(new ConstantColor(new RGBColor(0.7, 1, 0.6)));
			ringColor.setKR(1);
			Transformation ringTransformation = Transformation.translate(0, -0.95, -1.3)
					.append(Transformation.scale(0.2, 0.2, 0.2));

			BoundingVolume bvh = new BoundingVolume();

			Mesh mesh = OBJFileLoader.loadOBJ("res/models/ring.obj");
			for (int i = 0; i < mesh.indices.length; i += 3) {
				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
						ringColor));
			}
			bvh.calculateHierarchy();
			world.shapes.add(new Instance(bvh, true, ringTransformation, null));

			world.tracer = new HybridPathTracing(world);

			world.camera = new PerspectiveCamera(width, height, new Point3d(0.2, -0.6, -1.3),
					new Point3d(0, -0.95, -1.3), new Vector3d(0, 1, 0), 90);

			World.name = "hybrid_caustics_";
		} else if (scene.equals(APPLE_WITH_FLOOR_WITH_BHV)) {
			try {
				Transformation appleTransformation = Transformation.translate(0, -1, -2)
						.append(Transformation.rotateX(90));

				BoundingVolume bvh = new BoundingVolume();

				BufferedImage image = ImageIO.read(new File("res/textures/apple_texture.jpg"));

				SVMatte imageMatteApple = new SVMatte(
						new ImageTexture(image.getWidth(), image.getHeight(), image, null));
				imageMatteApple.setKA(0);
				imageMatteApple.setKD(0.7);
				Mesh mesh = OBJFileLoader.loadOBJ("res/models/apple.obj");
				for (int i = 0; i < mesh.indices.length; i += 3) {
					bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
							mesh.indices[i + 2], imageMatteApple));
				}
				bvh.calculateHierarchy();
				world.shapes.add(new Instance(bvh, true, appleTransformation, null));
			} catch (IOException e) {
				System.out.println("can't read the image apple_texture.jpg");
				System.exit(1);
			}

			SVMatte checkerMatte = new SVMatte(new Checker3D(1, new RGBColor(), new RGBColor(1, 1, 1)));
			checkerMatte.setKA(0);
			checkerMatte.setKD(0.7);
			world.shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), checkerMatte));

			Emissive emissive = new Emissive();
			emissive.setPower(10.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle = new Rectangle(new Point3d(-0.2, 0.999, -1.5), new Vector3d(0, 0, 0.4),
					new Vector3d(0.4, 0, 0), new Vector3d(0, -1, 0), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			Light arealight = new AreaLight(lightRectangle);
			arealight.setShadows(true);
			world.lights.add(arealight);

			world.tracer = new AreaLighting(world);

			world.camera = new PerspectiveCamera(width, height, new Point3d(0, 0, 0), new Point3d(0, 0, -1),
					new Vector3d(0, 1, 0), 90);
		} else if (scene.equals(APPLE_WITH_FLOOR_WITHOUT_BHV)) {
			try {
				Transformation appleTransformation = Transformation.translate(0, -1, -2)
						.append(Transformation.rotateX(90));

				BufferedImage image = ImageIO.read(new File("res/textures/apple_texture.jpg"));

				SVMatte imageMatteApple = new SVMatte(
						new ImageTexture(image.getWidth(), image.getHeight(), image, null));
				imageMatteApple.setKA(0);
				imageMatteApple.setKD(0.7);
				Mesh mesh = OBJFileLoader.loadOBJ("res/models/apple.obj");
				for (int i = 0; i < mesh.indices.length; i += 3) {
					world.shapes.add(new Instance(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1],
							mesh.indices[i + 2], imageMatteApple), true, appleTransformation, null));
				}
			} catch (IOException e) {
				System.out.println("can't read the image apple_texture.jpg");
				System.exit(1);
			}

			SVMatte checkerMatte = new SVMatte(new Checker3D(1, new RGBColor(), new RGBColor(1, 1, 1)));
			checkerMatte.setKA(0);
			checkerMatte.setKD(0.7);
			world.shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), checkerMatte));

			Emissive emissive = new Emissive();
			emissive.setPower(10.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle = new Rectangle(new Point3d(-0.2, 0.999, -1.5), new Vector3d(0, 0, 0.4),
					new Vector3d(0.4, 0, 0), new Vector3d(0, -1, 0), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			Light arealight = new AreaLight(lightRectangle);
			arealight.setShadows(true);
			world.lights.add(arealight);

			world.tracer = new AreaLighting(world);

			world.camera = new PerspectiveCamera(width, height, new Point3d(0, 0, 0), new Point3d(0, 0, -1),
					new Vector3d(0, 1, 0), 90);
		} else if (scene.equals(CAUSTICS_HYBRID_PATH_TRACING2)) {
			Emissive emissive = new Emissive();
			emissive.setPower(20.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle = new Rectangle(new Point3d(0.3, 0.4, 1), new Vector3d(0, 0, 0.3),
					new Vector3d(0.3, -0.3, 0), new Vector3d(0, -1, -1), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			Light arealight = new AreaLight(lightRectangle);
			arealight.setShadows(true);
			world.lights.add(arealight);

			BoundingVolume bvh = new BoundingVolume();
//
//			// reflective ring
			SVReflective ringColor = new SVReflective(new ConstantColor(new RGBColor(0.7, 0.3, 0.2)));
			ringColor.setKR(1);
			Transformation ringTransformation = Transformation.translate(0, -0.9, -1)
					.append(Transformation.scale(1, 0.5, 1));
			Mesh mesh = OBJFileLoader.loadOBJ("res/models/ring.obj");
			for (int i = 0; i < mesh.indices.length; i += 3) {
				bvh.addObject(new SmoothUVMeshTriangle(mesh, mesh.indices[i], mesh.indices[i + 1], mesh.indices[i + 2],
						ringColor));
			}
			bvh.calculateHierarchy();
			world.shapes.add(new Instance(bvh, true, ringTransformation, null));
			
			SVMatte floorColor = new SVMatte(new ConstantColor(new RGBColor(0.8, 0.8, 0.2)));
			floorColor.setKD(0.8);
			world.shapes.add(new Plane(new Point3d(0, -1, 0), new Vector3d(0, 1, 0), floorColor));
			
			world.tracer = new HybridPathTracing(world);

			world.camera = new PerspectiveCamera(width, height, new Point3d(-0.3, 0.3, 1.5),
					new Vector3d(0.1, -0.4, -1), new Vector3d(0, 1, 0), 70);

			World.name = "hybrid_caustics_";
		} 

	}

	private static void createCornellBoxWithoutLight(int width, int height, World world) {
		// create cornell box
		SVMatte leftWallColor = new SVMatte(new ConstantColor(new RGBColor(1, 0.1, 0.1)));
		leftWallColor.setKA(0.0);
		leftWallColor.setKD(0.7);
		Rectangle leftWall = new Rectangle(new Point3d(-1, -1, 0), new Vector3d(0, 0, -2), new Vector3d(0, 2, 0),
				new Vector3d(1, 0, 0), leftWallColor);
		world.shapes.add(leftWall);

		SVMatte rightWallColor = new SVMatte(new ConstantColor(new RGBColor(0.1, 1, 0.1)));
		rightWallColor.setKA(0.0);
		rightWallColor.setKD(0.7);
		Rectangle rightWall = new Rectangle(new Point3d(1, -1, 0), new Vector3d(0, 0, -2), new Vector3d(0, 2, 0),
				new Vector3d(-1, 0, 0), rightWallColor);
		world.shapes.add(rightWall);

		SVMatte backWallColor = new SVMatte(new ConstantColor(new RGBColor(0.5, 0.7, 0.5)));
		backWallColor.setKA(0.0);
		backWallColor.setKD(0.7);
		Rectangle backWall = new Rectangle(new Point3d(-1, -1, -2), new Vector3d(0, 2, 0), new Vector3d(2, 0, 0),
				new Vector3d(0, 0, 1), backWallColor);
		world.shapes.add(backWall);

		SVMatte floorColor = new SVMatte(new ConstantColor(new RGBColor(0.9, 0.7, 0.5)));
		floorColor.setKA(0.0);
		floorColor.setKD(0.7);
		Rectangle floor = new Rectangle(new Point3d(-1, -1, 0), new Vector3d(0, 0, -2), new Vector3d(2, 0, 0),
				new Vector3d(0, 1, 0), floorColor);
		world.shapes.add(floor);

		SVMatte ceilingColor = new SVMatte(new ConstantColor(new RGBColor(0.9, 0.7, 0.8)));
		ceilingColor.setKA(0.0);
		ceilingColor.setKD(0.7);
		Rectangle ceiling = new Rectangle(new Point3d(-1, 1, 0), new Vector3d(0, 0, -2), new Vector3d(2, 0, 0),
				new Vector3d(0, -1, 0), ceilingColor);
		world.shapes.add(ceiling);

		SVMatte frontWallColor = new SVMatte(new ConstantColor(new RGBColor(0.9, 0.7, 0.8)));
		frontWallColor.setKA(0.0);
		frontWallColor.setKD(0.7);
		Rectangle frontWall = new Rectangle(new Point3d(-1, -1, 0), new Vector3d(0, 2, 0), new Vector3d(2, 0, 0),
				new Vector3d(0, 0, -1), frontWallColor);
		world.shapes.add(frontWall);
	}

	private static void createCornellBoxLight(int num, boolean big, World world) {
		if (num == 1) {
			// create light
			Emissive emissive = new Emissive();
			emissive.setPower(20.0);
			emissive.setCE(1, 1, 1);
			Rectangle lightRectangle;
			if (big)
				lightRectangle = new Rectangle(new Point3d(-0.6, 0.999, -1.9), new Vector3d(0, 0, 1.2),
						new Vector3d(1.2, 0, 0), new Vector3d(0, -1, 0), emissive);
			else
				lightRectangle = new Rectangle(new Point3d(-0.2, 0.999, -1.5), new Vector3d(0, 0, 0.4),
						new Vector3d(0.4, 0, 0), new Vector3d(0, -1, 0), emissive);
			lightRectangle.setShadows(true);
			world.shapes.add(lightRectangle);

			world.lights.add(new AreaLight(lightRectangle));
		} else if (num == 2) {
			// create light
			Emissive emissive = new Emissive();
			emissive.setPower(1.0);
			emissive.setCE(1, 1, 1);

			Rectangle lightRectangle1 = new Rectangle(new Point3d(-0.6, 0.999, -1.5), new Vector3d(0, 0, 0.4),
					new Vector3d(0.4, 0, 0), new Vector3d(0, -1, 0), emissive);
			world.shapes.add(lightRectangle1);

			world.lights.add(new AreaLight(lightRectangle1));

			Rectangle lightRectangle2 = new Rectangle(new Point3d(0.2, 0.999, -1.5), new Vector3d(0, 0, 0.4),
					new Vector3d(0.4, 0, 0), new Vector3d(0, -1, 0), emissive);
			world.shapes.add(lightRectangle2);

			world.lights.add(new AreaLight(lightRectangle2));
		}
	}

}
