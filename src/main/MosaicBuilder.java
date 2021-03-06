package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.TaskInfoType;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.exceptions.ImageTooBigException;
import main.gui.GUICallback;
import main.gui.JFXGui;
import main.images.AvgRGB;
import main.images.ImageGrid;
import main.images.reader.ImageLibrary;
import pt.runtime.WorkerThread;
import pu.loopScheduler.LoopRange;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;
/**
 * 
 * @author DarkIris3196
 *
 */
public class MosaicBuilder {
	//Inputs
	private Map<String, AvgRGB> libraryIndex = null;
	private ImageGrid cellMatrix = null;
	private ImageLibrary imglib = null;
	private Set<String> keySet = null;
	private char type;
	
	private int cellHeight;
	private int cellWidth;
	private BufferedImage output;
	private Graphics2D g2d;
	
	private int numberOfCells;
	private double count;
	private boolean isFinished;
	
	private ProgressBar progressBar;
	private Label progressLabel;
	private Image outputImage;
	
	static long startTime;
	static long endTime;
	
	@Future
	private Void[] futureGroup = new Void[1];
	
	public MosaicBuilder(ProgressBar progressBar, Label progressLabel) {
		this.progressBar = progressBar;
		this.progressLabel = progressLabel;
	}
	
	public int createMosaic(ImageLibrary lib, Map<String, AvgRGB> libraryIndex, ImageGrid cellMatrix, int numOfThreads, char type, GUICallback callback, long firstTime) {
		long startTime = System.currentTimeMillis();
		this.imglib = lib;
		this.libraryIndex = libraryIndex;
		this.cellMatrix = cellMatrix;
		this.type = type;
		
		System.out.println("Starting CreateMosaic");
		
		keySet = libraryIndex.keySet();
		
		
		BufferedImage cell = imglib.getImage(keySet.iterator().next());
		cellHeight = (int)(cell.getHeight());
		cellWidth = (int)(cell.getWidth());
		
		int numberOfPixels = cellWidth * cellMatrix.getWidth() * cellHeight * cellMatrix.getHeight();
		
		if (numberOfPixels > 32000000) {
			throw new ImageTooBigException();
		}
		
		output = new BufferedImage(cellWidth*cellMatrix.getWidth(),cellHeight*cellMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
		g2d = output.createGraphics();
		
		numberOfCells = cellMatrix.getWidth() * cellMatrix.getHeight();
		
		count = 0;
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, cellMatrix.getHeight(), 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		
		@Future(taskType = TaskInfoType.MULTI)
		Void task = processMatrix(scheduler);
		futureGroup[0] = task;
		waitTillFinished();
		g2d.dispose();
		
		//return time of task execution
		long finalTime = System.currentTimeMillis();
		callback.setTime("mosaic", finalTime-startTime);
		callback.setTime("overall", finalTime-firstTime);
		System.out.println("Finish CreateMosaic");
		return 1;
	}

	public Void processMatrix(LoopScheduler scheduler) {
		WorkerThread worker = (WorkerThread) Thread.currentThread();
		LoopRange range = scheduler.getChunk(worker.getThreadID());
		
		if(range != null) {
			for(int row=range.loopStart; row<range.loopEnd; row++) {
				for(int col=0; col<cellMatrix.getWidth(); col++) {
					
					String minPointer = "";
					double minDistance = Math.pow(256, 3);					
					
					for (String key: keySet) {
						double checkDistance = calcDist(cellMatrix.getGridCell(col, row), libraryIndex.get(key), type);
			
							if (checkDistance < minDistance) {	
								minPointer = key;
								minDistance = checkDistance;
							}
					}	
					
					g2d.drawImage(imglib.getImage(minPointer), 
							col*cellWidth, row*cellHeight,  null);
				}
				
				outputImage = SwingFXUtils.toFXImage(output, null);
				@Gui
				Void gui = updateProgress();
			}
		}
		
		return null;
	}
	
	double calcDist(AvgRGB a, AvgRGB b, char type) {
		int aR = a.getR();
		int aG = a.getG();
		int aB = a.getB();
		int bR = b.getR();
		int bG = b.getG();
		int bB = b.getB();

		int delR = aR - bR;
		int delG = aG - bG;
		int delB = aB - bB;
		
		if (type == 'E') {
			//Euclidean algorithm
			return (Math.pow(delR, 2) + Math.pow(delG, 2) + Math.pow(delB, 2));
		}

		else if (type == 'R') {
			//Riemersma Metric
			double rAverage = (aR + bR)/2.0;
			double secR = 2 + rAverage/256 * Math.pow(delR, 2);
			double secG = 4 * Math.pow(delG, 2);
			double secB = 2 + 255 * rAverage/256 * Math.pow(delB, 2);
			return (secR + secG + secB);
		}
		
		else return 256*256*256;
	}
	
	public void waitTillFinished() {
		Void barrier = futureGroup[0];
	}
	
	public Void updateProgress() {
		count += cellMatrix.getWidth();
		progressBar.setProgress((float)count / numberOfCells);
		progressLabel.setText("Substituted " + (int)count + " out of " + numberOfCells + " cells");
		return null;
	}
	
	public Void postExecutionUpdate(ImageView imageView, Button saveButton, Button runButton, GUICallback callback) {
		isFinished = true;
		saveButton.setDisable(false);
		runButton.setDisable(false);
		imageView.setImage(SwingFXUtils.toFXImage(output, null));
		progressLabel.setText("Finished");
		callback.updateLatest();
		return null;
	}
	
	public int saveImageOnDisk(File saveToFile) {
		try {
			System.out.println("Saving image to disk");
			ImageIO.write(output, "png", saveToFile);
			System.out.println("Finished saving image to disk");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 1;
	}
	
	public Void enableSave(Button saveButton) {
		System.out.println("enabling save button");
		saveButton.setDisable(false);
		return null;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public Image getOutputImage() { 
		return outputImage;
	}
}
