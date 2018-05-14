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

public class MosaicBuilder {
	private ImageLibrary imglib;
	
	private String[][] mosaicMatrix;
	
	int cellHeight;
	int cellWidth;
	BufferedImage output;
	Graphics2D g2d ;
	
	@Future
	private Void[] futureGroup = new Void[1];
	
	public MosaicBuilder(ImageLibrary lib, String[][] matrix) {
		imglib = lib;
		mosaicMatrix = matrix;
	}
	
	public void createMosaic(double scale, int numOfThreads) {
		BufferedImage cell = imglib.getImage(mosaicMatrix[0][0]);
		cellHeight = (int)(cell.getHeight()*scale);
		cellWidth = (int)(cell.getWidth()*scale);
		output = new BufferedImage(cellWidth*mosaicMatrix[0].length,cellHeight*mosaicMatrix.length,BufferedImage.TYPE_INT_RGB);
		g2d = output.createGraphics();
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, mosaicMatrix.length, 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		@Future(taskType = TaskInfoType.MULTI)
		Void task = processMatrix(scheduler);
		futureGroup[0] = task;

		waitTillFinished();
		g2d.dispose();
		try {
			ImageIO.write(output, "png", new File("output.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void createMosaic() {
		createMosaic(1.0,1);
	}
	public void createMosaic(int numOfThreads) {
		createMosaic(1.0,numOfThreads);
	}
	
	public Void processMatrix(LoopScheduler scheduler) {
		WorkerThread worker = (WorkerThread) Thread.currentThread();
		LoopRange range = scheduler.getChunk(worker.getThreadID());
		
		for(int row=0; row<mosaicMatrix.length; row++) {
			for(int col=0; col<mosaicMatrix[0].length; col++) {
				g2d.drawImage(imglib.getImage(mosaicMatrix[row][col]), 
						col*cellWidth, row*cellHeight,  null);
			}
		}
		return null;
	}
	
	public void waitTillFinished() {
		Void barrier = futureGroup[0];
	}
}
