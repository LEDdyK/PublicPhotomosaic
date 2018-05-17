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
	public ImageGrid(BufferedImage image ) {
		img = image;
	}
	
	public int createGrid(boolean initialiseWithGridSize, int width, int height) {
		System.out.println("Starting ImageGrid");
		if (initialiseWithGridSize) {
			h = height;
			w = width;
			cellHeight = img.getHeight()/h;
			cellWidth = img.getWidth()/w;
		}
		
		else {
			cellHeight = height;
			cellWidth = width;
			h = img.getHeight() / cellHeight;
			w = img.getWidth() / cellWidth;
		}
		
		rgbList = new AvgRGB[h][w];
		
		for(int y=0; y<h; y++) {
			for(int x=0; x<w;x++) {
				
				rgbList[y][x] = new AvgRGB(img.getSubimage(x*cellWidth, y*cellHeight, cellWidth, cellHeight));
			}
		}
		System.out.println("Finished ImageGrid");
		return 0;
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
