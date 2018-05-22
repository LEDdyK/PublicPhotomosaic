package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.Gui;
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
	
	private static void printTimeStamp(String str) {
		prevTime = endTime;
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(str + "\n " + endTime);
		System.out.println("\tprocess time: " + (endTime-prevTime));
	}
}
