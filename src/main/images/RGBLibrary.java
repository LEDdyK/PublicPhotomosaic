package main.images;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * takes the average 
 * @author DarkIris3196
 *
 */
public class RGBLibrary {
	
	private HashMap<String,ImageRGB> rgbList;
	
	public RGBLibrary(HashMap<String,BufferedImage> library) {
		rgbList = new HashMap<String,ImageRGB>();
		for(String imageKey: library.keySet()) {
			BufferedImage image = library.get(imageKey);
			
			
			ImageRGB rgb = new ImageRGB(image);
			rgbList.put(imageKey,rgb);
		}
	}

	public void indexFiler(String fileName) {
		try {
			File file = new File(fileName);
			file.delete();
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for(String imageKey : rgbList.keySet()) {
				ImageRGB rgb = rgbList.get(imageKey);
				String line = imageKey + " " + rgb.getRed() + " " + rgb.getGreen() + " " + rgb.getBlue();
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
	
	public HashMap<String,ImageRGB> getRGBList(){
		return rgbList;
	}
	
	public ImageRGB getImageRGB(String fileName) {
		return rgbList.get(fileName);
	}
}
