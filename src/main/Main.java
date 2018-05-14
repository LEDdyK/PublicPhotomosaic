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
	
	@InitParaTask
	public static void main(String[] args) {
	try {
			long startTime = System.currentTimeMillis();

			ImageDownloader imageDownloader = new ImageDownloader();
			imageDownloader.downloadRecentImages(1);
			imageDownloader.waitTillFinished();
			
			long endTime = System.currentTimeMillis() - startTime;
			System.out.println(endTime);
			
			ImageLibrary imglib = new ImageLibrary("photos",1);
			
			RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
			
			BufferedImage image = ImageIO.read(new File("testPhotos/oliver.png"));
			ImageGrid imgGrid = new ImageGrid(false, 2,2, image);
			
			ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
			
			
			MosaicBuilder mosaicBuilder = new MosaicBuilder(imglib, imgTinder.findMatches('R'));
			mosaicBuilder.createMosaic();
			
	
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Void waitUp() {
		return null;
	}
}
