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
			
			//new ImageDownloader().downloadRecentImages();
			
			ImageLibrary imglib = new ImageLibrary("photos",0.45);
			
			RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
			
			BufferedImage image = ImageIO.read(new File("input.png"));
			ImageGrid imgGrid = new ImageGrid(false, 3,3, image);
			
			ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
			
			
			MosaicBuilder mosaicBuilder = new MosaicBuilder(imglib, imgTinder.findMatches('R'));
			mosaicBuilder.createMosaic();
			
			long endTime = System.currentTimeMillis() - startTime;
			System.out.println(endTime);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
