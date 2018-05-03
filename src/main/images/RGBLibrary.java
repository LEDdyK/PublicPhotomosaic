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
			int height = image.getHeight();
			int width = image.getWidth();
			
			//[0]red,[1]green,[2]blue
			long totalColor[] = new long[3];
			for(int j=0 ; j<height; j++) { //y axis
				for(int i=0 ; i<width; i++) { //x axis
					totalColor[0] = (image.getRGB(1, 1)>>16) & 0xff;
					totalColor[1] = (image.getRGB(1, 1)>>8) & 0xff;
					totalColor[2] = (image.getRGB(1, 1)) & 0xff;
				}
			}
			int aveRed = (int) (totalColor[0]/255);
			int aveGreen = (int) (totalColor[1]/255);
			int aveBlue = (int) (totalColor[2]/255);
			
			ImageRGB rgb = new ImageRGB(aveRed,aveGreen,aveBlue,imageKey);
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
