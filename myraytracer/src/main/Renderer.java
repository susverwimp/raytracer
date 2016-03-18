package main;

import math.Point3d;
import math.Vector3d;
import tracer.World;
import camera.PerspectiveCamera;
import film.FrameBuffer;
import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

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
	public static void main(String[] arguments) {
		int width = 620;
		int height = 620;
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
				new Point3d(0, 0, 0), new Point3d(0, 0, -1), new Vector3d(0, 1, 0), 90);

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
		 * Build the world
		 *********************************************************************/
		
		final World world = new World(camera, buffer, reporter, panel);
		
		/**********************************************************************
		 * Render the scene
		 *********************************************************************/
		
//		world.renderScene();
		world.renderFalseColor();

		/**********************************************************************
		 * Export the result
		 *********************************************************************/

		world.exportResult(sensitivity, gamma);
	}
}
