package main.images.reader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.TaskInfoType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import main.gui.JFXGui;
import pt.runtime.WorkerThread;
import pu.loopScheduler.LoopRange;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;

/**
 * This class will take in all image files in a certain folder and put them in a BufferedImage.
 * @author DarkIris3196
 *
 */
public class ImageLibrary {
	
	private double libCount;
	
	private Map<String,BufferedImage> library;
	private File[] directoryListing;
	private List<File> toDelete = null;
	
	private ProgressBar progressBar;
	private Label progressLabel;
	
	@Future
	private Void[] futureGroup = new Void[1];

	public ImageLibrary(ProgressBar progressBar, Label progressLabel) {
		this.progressBar = progressBar;
		this.progressLabel = progressLabel;
	}
	
	/**
	 * processes a directory
	 * @param dirPath
	 */
	public Map<String, BufferedImage> readDirectory(String dirPath) {
		return readDirectory(dirPath,1.0,1);
		
	}
	
	/**
	 * processes a directory
	 * @param dirPath
	 */
	public Map<String, BufferedImage> readDirectory(String dirPath, double scale, int numOfThreads) {
		System.out.println("Starting ImageLibrary");
		File directory = new File(dirPath);
		directoryListing = directory.listFiles();
		library = Collections.synchronizedMap(new HashMap<String,BufferedImage>());
		toDelete = new ArrayList<File>();
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, directoryListing.length, 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		libCount = 0;
		@Future(taskType = TaskInfoType.MULTI)
		Void task = processDirectory(scheduler,scale);
		futureGroup[0] = task;
		
		waitTillFinished();
		System.out.println(library.size() + " images in library");
		System.out.println("Finished ImageLibrary");
		
		for(File deletable: toDelete) {
			if(!deletable.delete()) {
				System.err.println("Image " + deletable.getName() + " deletion failed");
			}
		}
		return library;
	}
	
	public Void processDirectory(LoopScheduler scheduler, double scale) {
		WorkerThread worker = (WorkerThread) Thread.currentThread();
		LoopRange range = scheduler.getChunk(worker.getThreadID());
		File file = null;
		if(range != null && directoryListing != null) {
			for(int i = range.loopStart; i<range.loopEnd; i++) {
				try {
					file = directoryListing[i];
					BufferedImage image = ImageIO.read(file);
					int w = (int)(image.getWidth()*scale);
					int h = (int)(image.getHeight()*scale);
					BufferedImage scaledImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d = scaledImage.createGraphics();
					g2d.drawImage(image,0,0,w,h,null);
					
					if(image.getHeight() == image.getWidth()) {
						library.put(file.getName(),scaledImage);
					}else {
						toDelete.add(file);
					}
				} catch (IOException e) {
					System.err.println("File is not compatible or is not an image file.");
					e.printStackTrace();
				} catch(IllegalArgumentException e) {
					System.err.println("Bad Image detected: "+file.getName());
				}
				@Gui
				Void progress = updateProgress();
			}
		}
		
		
		return null;
	}
	
	/**
	 * processes a single image
	 * @param path
	 */
	public void readFile(String path) {
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
	
	public Map<String,BufferedImage> getLibrary(){
		return library;
	}
	
	public BufferedImage getImage(String key) {
		return library.get(key);
	}
	
	public void waitTillFinished() {
		Void barrier = futureGroup[0];
	}
	
	private Void updateProgress() {
		++libCount;
		progressBar.setProgress((float)libCount/directoryListing.length);
		progressLabel.setText("Processed " + (int)libCount + " out of " + directoryListing.length + " images");
		return null;
	}
	
	public Void postExecutionUpdate() {
		progressLabel.setText("Finished");
		return null;
	}
	
}
