package main.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class will take in all image files in a certain folder and put them in a BufferedImage.
 * @author DarkIris3196
 *
 */
public class ImageReader {
	
	HashMap<String,BufferedImage> library;
	
	/**
	 * loop through all the files in the directory specified in the path
	 * @param dirPath
	 */
	public ImageReader(String dirPath) {
		processDirectory(dirPath);
	}
	
	public void processDirectory(String dirPath) {
		File directory = new File(dirPath);
		File[] directoryListing = directory.listFiles();
		library = new HashMap<String,BufferedImage>();
		
		if(directoryListing != null) {
			for(File file : directoryListing) {
				try {
					BufferedImage image = ImageIO.read(file);
					library.put(file.getName(),image);
				} catch (IOException e) {
					System.err.println("File is not compatible or is not an image file.");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void processFile(String path) {
		File file = new File(path);
		library = new HashMap<String,BufferedImage>();
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			library.put(file.getName(),image);
		} catch (IOException e) {
			System.err.println("File is not compatible or is not an image file.");
			e.printStackTrace();
		}
	}
	
	public HashMap<String,BufferedImage> getLibrary(){
		return library;
	}
}
