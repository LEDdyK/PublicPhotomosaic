package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.InitParaTask;
import apt.annotations.TaskScheduingPolicy;
import javafx.application.Application;
import main.gui.JFXGui;
import main.images.AvgRGB;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;
import pt.runtime.ParaTask;

public class Main {
	static long startTime;
	static long endTime = 0;
	static long prevTime;
	
	@InitParaTask
	public static void main(String[] args) {
		
		Application.launch(JFXGui.class, args);
		//runComputations();
	}

	public static void runComputations() {
		try {
			
			//reset settings
			JFXGui.downProp.setCount(0);
			JFXGui.imgLibProp.setCount(0);
			JFXGui.tinSubProp.setCount(0);
			JFXGui.outProp.setCount(0);
			JFXGui.outImage = null;
			JFXGui.hboxOut.getChildren().remove(JFXGui.dispOut);
			
			startTime = System.currentTimeMillis();

			ImageDownloader imageDownloader = new ImageDownloader();
			@Future
			int imageDownload = imageDownloader.downloadRecentImages(4);

			ImageLibrary imglib = new ImageLibrary();
			@Future(depends="imageDownload")
			Map<String, BufferedImage> imgLibrary = imglib.readDirectory("photos", Double.parseDouble(JFXGui.libScale.getText()), Integer.parseInt(JFXGui.threadCount.getText()));	
						
			BufferedImage image = ImageIO.read(new File(JFXGui.refPath.getText()));
			ImageGrid imgGrid = new ImageGrid(image);
			@Future()
			int imageGrid = imgGrid.createGrid(false, Integer.parseInt(JFXGui.gridWidth.getText()), Integer.parseInt(JFXGui.gridHeight.getText()));	

			
			RGBLibrary rgbLib = new RGBLibrary();
			@Future()
			Map<String, AvgRGB> rgbList = rgbLib.calculateRGB(imgLibrary);
			
			
			MosaicBuilder mosaicBuilder = new MosaicBuilder();
			@Future()
			int mosaicBuild = mosaicBuilder.createMosaic(imglib, rgbList, imgGrid, Integer.parseInt(JFXGui.threadCount.getText()), 'R');
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void printTimeStamp(String str) {
		prevTime = endTime;
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(str + "\n " + endTime);
		System.out.println("\tprocess time: " + (endTime-prevTime));
	}
}
