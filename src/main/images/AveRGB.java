package main.images;

import java.awt.image.BufferedImage;
/**
 * calculates the average RGB of the image
 * @author DarkIris3196
 *
 */
public class AveRGB {
	private int red;
	private int green;
	private int blue;
	
	public AveRGB(BufferedImage image){
		int height = image.getHeight();
		int width = image.getWidth();
		int pixelNum = height*width;
		//[0]red,[1]green,[2]blue
		long totalColor[] = new long[3];
		for(int j=0 ; j<height; j++) { //y axis
			for(int i=0 ; i<width; i++) { //x axis
				totalColor[0] += (image.getRGB(i, j)>>16) & 0xff;
				totalColor[1] += (image.getRGB(i, j)>>8) & 0xff;
				totalColor[2] += (image.getRGB(i, j)) & 0xff;
			}
		}
		red= (int) (totalColor[0]/pixelNum);
		green = (int) (totalColor[1]/pixelNum);
		blue = (int) (totalColor[2]/pixelNum);
	}

	public int getR() {
		return red;
	}
	public int getG() {
		return green;
	}
	public int getB() {
		return blue;
	}
}
