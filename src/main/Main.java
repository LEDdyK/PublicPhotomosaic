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

			ImageDownloader imageDownloader = new ImageDownloader();
			imageDownloader.downloadRecentImages(4);
			imageDownloader.waitTillFinished();		
			printTimeStamp("ImageDownloader");
			
			ImageLibrary imglib = new ImageLibrary("photos", 1);		
			printTimeStamp("ImageLibrary");
			
			RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
			printTimeStamp("RGBLibrary");
			
			BufferedImage image = ImageIO.read(new File("testPhotos/oliver.png"));
			ImageGrid imgGrid = new ImageGrid(false, 2, 2, image);			
			printTimeStamp("ImageGrid");
			
			ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
			printTimeStamp("ImageTinder");
			
			MosaicBuilder mosaicBuilder = new MosaicBuilder(imglib, imgTinder.findMatches('R'));
			mosaicBuilder.createMosaic(4);
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
