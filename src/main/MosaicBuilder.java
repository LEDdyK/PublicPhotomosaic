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
	Set<String> keySet = null;
	private char type;
	//private String[][] mosaicMatrix;
	
	int cellHeight;
	int cellWidth;
	BufferedImage output;
	Graphics2D g2d;
	double count;
	

	static long startTime;
	static long endTime;
	
	@Future
	private Void[] futureGroup = new Void[1];
	
	public MosaicBuilder(ImageLibrary lib, String[][] matrix) {
		imglib = lib;
	}
	
	public MosaicBuilder() {
		
	}
	
	public int createMosaic(ImageLibrary lib, Map<String, AvgRGB> libraryIndex, ImageGrid cellMatrix, int numOfThreads, char type) {
		this.imglib = lib;
		this.libraryIndex = libraryIndex;
		this.cellMatrix = cellMatrix;
		this.type = type;
		
		System.out.println("Starting CreateMosaic");
		//startTime = System.currentTimeMillis();
		
		keySet = libraryIndex.keySet();
		
		
		BufferedImage cell = imglib.getImage(keySet.iterator().next());
		cellHeight = (int)(cell.getHeight());
		cellWidth = (int)(cell.getWidth());
		output = new BufferedImage(cellWidth*cellMatrix.getWidth(),cellHeight*cellMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
		g2d = output.createGraphics();
		
		JFXGui.numberOfCells = cellMatrix.getWidth() * cellMatrix.getHeight();
		//printTimeStamp("MosaicInit");
		
		count = 0;
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, cellMatrix.getHeight(), 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		
		@Future(taskType = TaskInfoType.MULTI)
		Void task = processMatrix(scheduler);
		futureGroup[0] = task;
		waitTillFinished();
		g2d.dispose();
		
		//printTimeStamp("MosaicSubstitution " + numOfThreads);
		
		System.out.println("Finish CreateMosaic");
		JFXGui.finishedImage = output;
//		try {
//			System.out.println("Saving image to disk");
//			ImageIO.write(output, "jpg", new File("output.jpg"));
//			System.out.println("Finished saving image to disk");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		//printTimeStamp("MosaicWrite");
		return 1;
	}

//	public void createMosaic() {
//		createMosaic(1.0,1);
//	}
//	public void createMosaic(int numOfThreads) {
//		createMosaic(1.0,numOfThreads);
//	}
	
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
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
				}
				JFXGui.outImage = SwingFXUtils.toFXImage(output, null);
				@Gui
				Void gui = updateProgress();
			}
		}
		
		return null;
	}
	
	double calcDist(AvgRGB a, AvgRGB b, char type) {
		//System.out.println(libraryIndex);
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
	
	private static void printTimeStamp(String str) {
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(str + " - " + endTime);
		startTime = System.currentTimeMillis();
	}
	
	public Void updateProgress() {

		count += cellMatrix.getWidth();
		JFXGui.tinSubProgress.setProgress((float)count / JFXGui.numberOfCells);
		//System.out.println("HELLO");
		//JFXGui.tinSubProp.setCount(count);
		return null;
	}
	
	public Void displayOnGUI() {
		JFXGui.isFinished = true;
		JFXGui.dispOut.setImage(SwingFXUtils.toFXImage(output, null));
		return null;
	}
}
