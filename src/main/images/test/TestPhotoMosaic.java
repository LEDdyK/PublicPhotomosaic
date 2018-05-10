package main.images.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import main.ImageTinder;
import main.MosaicBuilder;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;

public class TestPhotoMosaic {

	@Test
	public void testPhotoMosaicWithDownload() throws Exception {
		new ImageDownloader().downloadRecentImages();
		createPhotoMosaic();
	}
	
	@Test
	public void testPhotoMosaicWithoutDownload() throws Exception {
		createPhotoMosaic();
	}
	
	private void createPhotoMosaic() throws IOException {
		System.out.println("Creating library of images");
		ImageLibrary imglib = new ImageLibrary("photos");
		
		System.out.println("Creating library of avg rgb values");
		RGBLibrary rgbLib = new RGBLibrary(imglib.getLibrary());
		
		System.out.println("Creating average rgb grid of target image");
		BufferedImage image = ImageIO.read(new File("testPhotos/anime.png"));
		ImageGrid imgGrid = new ImageGrid(false, 8, 8, image);
	
		System.out.println("Creating matrix of image substitutions");
		ImageTinder imgTinder = new ImageTinder(rgbLib.getRGBList(), imgGrid);
		MosaicBuilder mosaicBuilder = new MosaicBuilder(imglib, imgTinder.findMatches('R'));
		
		System.out.println("Creating the mosaic");
		mosaicBuilder.createMosaic();
	}
}
