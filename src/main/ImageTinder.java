package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.gui.JFXGui;
import main.images.AvgRGB;
import main.images.ImageGrid;

public class ImageTinder {
	/*
	IMPORTANT NOTE:
	The input for the reference image should be in cell-units and not pixel-units
	The output does not generate the mosaic image but generates a 2D array with each entry
	only labeling the index location of the best cell substitute in the image library 
	folder.
	*/
	
	//Compares each cell of a reference image with each image from a image library folder.
	//Assumptions:
		//1. The number of items in the library does not exceed 2 147 483 648
		//2. The number of cells in the reference image does not exceed 2 147 483 648

	//Inputs
	Map<String, AvgRGB> libraryIndex;
	ImageGrid cellMatrix;

	//Outputs
	String[][] mosaicMatrix;

	//Variables
	String minPointer;
	double minDistance;
	double cellCount;

	//Methods
	//Constructor
	public ImageTinder() {
		
	}
	//Reset pointer positions and distance to compare
	public void makeDefault() {
		minPointer = "";
		minDistance = Math.pow(256, 3);
	}
	//generate the mosaic matrix
	public int findMatches(Map<String, AvgRGB> libraryIndex, ImageGrid cellMatrix, char type) {
		System.out.println("Starting ImageTinder");
		this.libraryIndex = libraryIndex;
		this.cellMatrix = cellMatrix;
		makeDefault();
		mosaicMatrix = new String[cellMatrix.getHeight()][cellMatrix.getWidth()];
		cellCount = 0;
		
		for (int i = 0; i < cellMatrix.getHeight(); ++i) {
			for (int j = 0; j < cellMatrix.getWidth(); ++j) {
				
				//Default for each loop
				makeDefault();
	
				Set<String> keySet = libraryIndex.keySet();
				for (String key: keySet) {
					double checkDistance = calcDist(cellMatrix.getGridCell(j, i), libraryIndex.get(key), type);
		
						if (checkDistance < minDistance) {	
							minPointer = key;
							minDistance = checkDistance;
						}
				}
				
				//process mosaic matrix cell
				mosaicMatrix[i][j] = minPointer;
				updateCellCount();
			}
		}
		System.out.println("Finished ImageTinder");
		return 1;
	}
	
	public String[][] getMosaicMatrix() {
		return mosaicMatrix;
	}
	
	//calculate RGB distance
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
	
	public void updateCellCount() {
		++cellCount;
		JFXGui.tinSubProp.setCount(cellCount);
	}
}
