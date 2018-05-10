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
	private AveRGB[][] rgbList;
	public RGBGrid(int gridWidth, int gridHeight, BufferedImage image ) {
		img = image;
		h=gridHeight;
		w=gridWidth;
		cellHeight = image.getHeight()/h;
		cellWidth = image.getWidth()/w;
		rgbList = new AveRGB[h][w];
		for(int y=0; y<h; y++) {
			for(int x=0; x<w;x++) {
				
				rgbList[y][x] = new AveRGB(image.getSubimage(x*cellWidth, y*cellHeight, cellWidth, cellHeight));
			}
		}
	}
	
	public AveRGB getGridCell(int x,int y) {
		return rgbList[y][x];
	}
	
	public int getHeight() {
		return h;
	}
	public int getWidth() {
		return w;
	}
	
	public BufferedImage gridImage(int x,int y) {
		return img.getSubimage(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
	}
}
