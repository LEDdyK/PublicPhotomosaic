package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.images.reader.ImageLibrary;

public class MosaicBuilder {
	ImageLibrary imglib;
	
	String[][] mosaicMatrix;
	
	public MosaicBuilder(ImageLibrary lib, String[][] matrix) {
		imglib = lib;
		mosaicMatrix = matrix;
	}
	
	public void createMosaic() {
		BufferedImage cell = imglib.getImage(mosaicMatrix[0][0]);
		int cellHeight = cell.getHeight();
		int cellWidth = cell.getWidth();
		BufferedImage output = new BufferedImage(cellWidth*mosaicMatrix[0].length,cellHeight*mosaicMatrix.length,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = output.createGraphics();
		for(int row=0; row<mosaicMatrix.length; row++) {
			for(int col=0; col<mosaicMatrix[0].length; col++) {
				g2d.drawImage(imglib.getImage(mosaicMatrix[row][col]), 
						col*cellWidth, row*cellHeight,  null);
			}
		}
		g2d.dispose();
		try {
			ImageIO.write(output, "png", new File("output.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
