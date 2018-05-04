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

/**
 * stores the image RGB of all the images in the directory
 * @author DarkIris3196
 *
 */
public class RGBLibrary {
	
	private HashMap<String,AveRGB> rgbList;
	
	/**
	 * takes in a library of images and converts them into a library of RGBs paired with their filename
	 * @param library
	 */
	public RGBLibrary(HashMap<String,BufferedImage> library) {
		rgbList = new HashMap<String,AveRGB>();
		for(String imageKey: library.keySet()) {
			BufferedImage image = library.get(imageKey);
			AveRGB rgb = new AveRGB(image);
			rgbList.put(imageKey,rgb);
		}
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
			
			AveRGB rgb = new AveRGB(red,blue,green);
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
				AveRGB rgb = rgbList.get(imageKey);
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
	
	
	public HashMap<String,AveRGB> getRGBList(){
		return rgbList;
	}
	
	public AveRGB getImageRGB(String fileName) {
		return rgbList.get(fileName);
	}
}
