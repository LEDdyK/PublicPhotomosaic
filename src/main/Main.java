package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.InitParaTask;
import apt.annotations.TaskScheduingPolicy;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;
import pt.runtime.ParaTask;

public class Main {
	static long startTime;
	static long endTime;
	
	@InitParaTask
	public static void main(String[] args) {
	try {
			startTime = System.currentTimeMillis();
			//new ImageDownloader().downloadRecentImages(4);
			
			ImageLibrary imglib = new ImageLibrary("photos",0.45);
			
			printTimeStamp("ImageLibrary");
			
			RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
			
			printTimeStamp("RGBLibrary");
			
			BufferedImage image = ImageIO.read(new File("testPhotos/anime.png"));
			ImageGrid imgGrid = new ImageGrid(false, 3,3, image);
			
			printTimeStamp("ImageGrid");
			
			ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
			
			printTimeStamp("ImageTinder");
			
			MosaicBuilder mosaicBuilder = new MosaicBuilder(imglib, imgTinder.findMatches('R'));
			mosaicBuilder.createMosaic();
			
			printTimeStamp("MosaicBuilder");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printTimeStamp(String str) {
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(str + " - " + endTime);
	}
}
