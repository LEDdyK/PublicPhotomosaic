package main.images.reader;

import java.awt.Graphics2D;
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
public class ImageLibrary {
	
	HashMap<String,BufferedImage> library;
	
	/**
	 * loop through all the files in the directory specified in the path
	 * @param dirPath
	 */
	public ImageLibrary(String dirPath) {
		processDirectory(dirPath);
	}
	public ImageLibrary(String dirPath,double scale) {
		processDirectory(dirPath,scale);
	}
	
	/**
	 * processes a directory
	 * @param dirPath
	 */
	public void processDirectory(String dirPath) {
		processDirectory(dirPath,1.0);
		
	}
	
	/**
	 * processes a directory
	 * @param dirPath
	 */
	public void processDirectory(String dirPath, double scale) {
		File directory = new File(dirPath);
		File[] directoryListing = directory.listFiles();
		library = new HashMap<String,BufferedImage>();
		List<File> toDelete = new ArrayList<File>();
		if(directoryListing != null) {
			for(File file : directoryListing) {
				try {
					BufferedImage image = ImageIO.read(file);
					int w = (int)(image.getWidth()*scale);
					int h = (int)(image.getHeight()*scale);
					BufferedImage scaledImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d = scaledImage.createGraphics();
					g2d.drawImage(image,0,0,w,h,null);
					
					if(image.getHeight() == image.getWidth()) {
						library.put(file.getName(),scaledImage);
					}else{
						toDelete.add(file);
					}
				} catch (IOException e) {
					System.err.println("File is not compatible or is not an image file.");
					e.printStackTrace();
				}catch(IllegalArgumentException e){
					System.err.println("Bad Image detected: "+file.getName());
				}
			}
		}

		for(File deletable: toDelete) {
			if(!deletable.delete()) {
				System.err.println("Image " + deletable.getName() + " deletion failed");
			}
		}
	}
	
	/**
	 * processes a single image
	 * @param path
	 */
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
	
	public BufferedImage getImage(String key) {
		return library.get(key);
	}
}
