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
 * stores the image RGB of all the images in the directory
 * @author DarkIris3196
 *
 */
public class RGBLibrary {
	
	private HashMap<String,AveRGB> rgbList;
	
	public RGBLibrary(HashMap<String,BufferedImage> library) {
		rgbList = new HashMap<String,AveRGB>();
		for(String imageKey: library.keySet()) {
			BufferedImage image = library.get(imageKey);
			AveRGB rgb = new AveRGB(image);
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
