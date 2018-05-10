package main.images;

import java.awt.image.BufferedImage;

/**
 * stores the RGB of the image grid
 * @author DarkIris3196
 *
 */
public class ImageGrid {
	private int h;
	private int w;
	private int cellHeight;
	private int cellWidth;
	private BufferedImage img;
	private AvgRGB[][] rgbList;
	public ImageGrid(boolean initialiseWithGridSize, int width, int height, BufferedImage image ) {
		img = image;
		
		if (initialiseWithGridSize) {
			h = height;
			w = width;
			cellHeight = image.getHeight()/h;
			cellWidth = image.getWidth()/w;
		}
		
		else {
			cellHeight = height;
			cellWidth = width;
			h = image.getHeight() / cellHeight;
			w = image.getWidth() / cellWidth;
		}
		
		rgbList = new AvgRGB[h][w];
		
		for(int y=0; y<h; y++) {
			for(int x=0; x<w;x++) {
				
				rgbList[y][x] = new AvgRGB(image.getSubimage(x*cellWidth, y*cellHeight, cellWidth, cellHeight));
			}
		}
		System.out.println();
	}
	
	
	public AvgRGB getGridCell(int x,int y) {
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
