package main.images;

import java.awt.image.BufferedImage;
/**
 * calculates the average RGB of the image
 * @author DarkIris3196
 *
 */
public class ImageRGB {
	private int red;
	private int green;
	private int blue;
	
	public ImageRGB(BufferedImage image){
		int height = image.getHeight();
		int width = image.getWidth();
		
		//[0]red,[1]green,[2]blue
		long totalColor[] = new long[3];
		for(int j=0 ; j<height; j++) { //y axis
			for(int i=0 ; i<width; i++) { //x axis
				totalColor[0] = (image.getRGB(i, j)>>16) & 0xff;
				totalColor[1] = (image.getRGB(i, j)>>8) & 0xff;
				totalColor[2] = (image.getRGB(i, j)) & 0xff;
			}
		}
		red= (int) (totalColor[0]/255);
		green = (int) (totalColor[1]/255);
		blue = (int) (totalColor[2]/255);
	}

	public int getRed() {
		return red;
	}
	public int getGreen() {
		return green;
	}
	public int getBlue() {
		return blue;
	}
}
