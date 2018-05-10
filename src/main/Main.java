package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;

public class Main {

	public static void main(String[] args) {
	try {
			long startTime = System.currentTimeMillis();
			
			new ImageDownloader().downloadRecentImages();
			ImageLibrary imglib = new ImageLibrary("photos");
			RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
			
			BufferedImage image = ImageIO.read(new File("biggest.png"));
			ImageGrid imgGrid = new ImageGrid(100, 100, image);
			
			ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
			
			PhotoBuilder photoBuilder = new PhotoBuilder(imglib, imgTinder.findSubstitute('R'));
			photoBuilder.makePhoto();
			
			long endTime = System.currentTimeMillis() - startTime;
			System.out.println(endTime);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
