package main.images;

import java.awt.image.BufferedImage;

import apt.annotations.Gui;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import main.gui.GUICallback;
import main.gui.JFXGui;

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
	
	private ProgressBar progress;
	private Label progressLabel;
	private int progressCount;
	
	public ImageGrid(BufferedImage image, ProgressBar progress, Label progressLabel) {
		img = image;
		this.progress = progress;
		this.progressLabel = progressLabel;
	}
	
	public int createGrid(boolean initialiseWithGridSize, int width, int height, GUICallback callback) {
		long startTime = System.currentTimeMillis();
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
				@Gui
				Void update = updateProgress();
			}
		}
		callback.setTime("reference", System.currentTimeMillis()-startTime);
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
	
	private Void updateProgress() {
		progressCount++;
		progress.setProgress((float) progressCount / (h * w));
		progressLabel.setText("Calculated rgb for " + progressCount + " out of " + (h*w) + " cells");
		return null;
	}
	
	public Void postExecutionUpdate() {
		progressLabel.setText("Finished");
		return null;
	}
}
