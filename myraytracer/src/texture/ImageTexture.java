package texture;

import java.awt.image.BufferedImage;

import mapping.Mapping;
import math.RGBColor;
import math.Vector2i;
import util.ShadeRec;

public class ImageTexture extends Texture {

	private int hRes;
	private int vRes;
	private BufferedImage image;
	private Mapping mapping;
	
	public ImageTexture(int hRes, int vRes, BufferedImage image, Mapping mapping) {
		this.hRes = hRes;
		this.vRes = vRes;
		this.image = image;
		this.mapping = mapping;
	}
	
	@Override
	public RGBColor getColor(ShadeRec shadeRec) {
		Vector2i pixelCoords = new Vector2i(0, 0);
		if(mapping != null){
			mapping.getTexelCoords(shadeRec.localHitPoint, vRes, hRes, pixelCoords);
		}else{
			pixelCoords.x = (int) (shadeRec.textureCoords.x * (hRes - 1));
			pixelCoords.y = (int) (shadeRec.textureCoords.y * (vRes - 1));
		}
		
		int rgb = image.getRGB(pixelCoords.x, pixelCoords.y);
		double r = ((rgb & 0x00ff0000) >> 16) / 255.0;
		double g = ((rgb & 0x0000ff00) >> 8) / 255.0;
		double b =  (rgb & 0x000000ff) / 255.0;
		
		return new RGBColor(r,g,b);
	}

}
