package main.images;

import java.awt.image.BufferedImage;

/**
 * stores the RGB of the image grid
 * @author DarkIris3196
 *
 */
public class RGBGrid {
	private int h;
	private int w;
	private int cellHeight;
	private int cellWidth;
	private BufferedImage img;
	private ImageRGB[][] rgbList;
	public RGBGrid(int gridHeight, int gridWidth, BufferedImage image ) {
		h=gridHeight;
		w=gridWidth;
		cellHeight = image.getHeight()/h;
		cellWidth = image.getWidth()/w;
		rgbList = new ImageRGB[w][h];
		for(int y=0; y<h; y++) {
			for(int x=0; x<h;x++) {
				image.getSubimage(x, y, cellWidth, cellWidth);
				rgbList[x][h] = new ImageRGB(image);
			}
		}
	}
}
