package main.images;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import apt.annotations.Future;
import apt.annotations.TaskInfoType;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;

/**
 * stores the image RGB of all the images in the directory
 * @author DarkIris3196
 *
 */
public class RGBLibrary {
	
	private HashMap<String,AvgRGB> rgbList;
	
	/**
	 * takes in a library of images and converts them into a library of RGBs paired with their filename
	 * @param library
	 */
	public RGBLibrary(HashMap<String,BufferedImage> library) {
		rgbList = new HashMap<String,AvgRGB>();
		
		//int numOfThreads=4;
		//LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, library.size(), 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		//@Future(taskType=TaskInfoType.MULTI)
		Void task = calculateRGB(library);
	}
	
	public Void calculateRGB(HashMap<String,BufferedImage> library) {
		for(String imageKey: library.keySet()) {
			BufferedImage image = library.get(imageKey);
			AvgRGB rgb = new AvgRGB(image);
			rgbList.put(imageKey,rgb);
		}
		return null;
	}
	
	/**
	 * takes in an indexfile and converts the data into a library of RGBs paired with their filename
	 * @param indexFile
	 * @throws FileNotFoundException
	 */
	public RGBLibrary(File indexFile) throws FileNotFoundException {
		Scanner index = new Scanner(indexFile);
		while(index.hasNextLine()) {
			Scanner indexLine = new Scanner(index.nextLine());
			String fileName = indexLine.next();
			int red = Integer.parseInt(indexLine.next());
			int green = Integer.parseInt(indexLine.next());
			int blue = Integer.parseInt(indexLine.next());
			
			AvgRGB rgb = new AvgRGB(red,blue,green);
			rgbList.put(fileName,rgb);
			indexLine.close();
		}
		index.close();
	}

	/**
	 * creates an indexFile and writes to it the rgb values and their associated filenames
	 * @param fileName
	 */
	public void indexFiler(String fileName) {
		try {
			File file = new File(fileName);
			file.delete();
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for(String imageKey : rgbList.keySet()) {
				AvgRGB rgb = rgbList.get(imageKey);
				String line = imageKey + " " + rgb.getR() + " " + rgb.getG() + " " + rgb.getB();
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public HashMap<String,AvgRGB> getRGBList(){
		return rgbList;
	}
	
	public AvgRGB getImageRGB(String fileName) {
		return rgbList.get(fileName);
	}
}
