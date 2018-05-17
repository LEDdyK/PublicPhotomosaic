package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.TaskInfoType;
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
	private ImageLibrary imglib;
	
	private String[][] mosaicMatrix;
	
	int cellHeight;
	int cellWidth;
	BufferedImage output;
	Graphics2D g2d ;
	

	static long startTime;
	static long endTime;
	
	@Future
	private Void[] futureGroup = new Void[1];
	
	public MosaicBuilder(ImageLibrary lib, String[][] matrix) {
		imglib = lib;
		mosaicMatrix = matrix;
	}
	
	public MosaicBuilder() {
		
	}
	
	public int createMosaic(ImageLibrary lib, String[][] matrix, double scale, int numOfThreads) {
		imglib = lib;
		mosaicMatrix = matrix;
		
		System.out.println("Starting CreateMosaic");
		//startTime = System.currentTimeMillis();
		
		BufferedImage cell = imglib.getImage(mosaicMatrix[0][0]);
		cellHeight = (int)(cell.getHeight()*scale);
		cellWidth = (int)(cell.getWidth()*scale);
		output = new BufferedImage(cellWidth*mosaicMatrix[0].length,cellHeight*mosaicMatrix.length,BufferedImage.TYPE_INT_RGB);
		g2d = output.createGraphics();
		
		//printTimeStamp("MosaicInit");
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, mosaicMatrix.length, 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		@Future(taskType = TaskInfoType.MULTI)
		Void task = processMatrix(scheduler);
		futureGroup[0] = task;
		waitTillFinished();
		g2d.dispose();
		
		//printTimeStamp("MosaicSubstitution " + numOfThreads);
		
		System.out.println("Finish CreateMosaic");
		
		try {
			System.out.println("Saving image to disk");
			ImageIO.write(output, "jpg", new File("output.jpg"));
			System.out.println("Finished saving image to disk");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				for(int col=0; col<mosaicMatrix[0].length; col++) {
					g2d.drawImage(imglib.getImage(mosaicMatrix[row][col]), 
							col*cellWidth, row*cellHeight,  null);
				}
			}
		}
		
		return null;
	}
	
	public void waitTillFinished() {
		Void barrier = futureGroup[0];
	}
	
	private static void printTimeStamp(String str) {
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(str + " - " + endTime);
		startTime = System.currentTimeMillis();
	}
}
