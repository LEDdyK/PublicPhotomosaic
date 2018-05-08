package Photomosaic;

public class Compare {
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
	/*type*/ libraryIndex;
	Cell[][] cellMatrix;

	//Outputs
	int[][] mosaicMatrix;

	//Variables
	int pointer;
	int minPointer;
	double minDistance;

	//Methods
	//Constructor
	public void Comparisons(/*type*/ libraryIndex, Cell[][] cellMatrix) {
		this.libraryIndex = libraryIndex;
		this.cellMatrix = cellMatrix;
		makeDefault();
		mosaicMatrix = new Cell[cellMatrix.length()][cellMatrix[0].length()];
	}
	//Reset pointer positions and distance to compare
	public void makeDefault() {
		pointer = 0;
		minPointer = 0;
		minDistance = Math.pow(256, 3);
	}
	//generate the mosaic matrix
	void findSubstitute(char type) {
		for (int i = 0; i < cellMatrix.length; ++i) {
			for (int j = 0; j < cellMatrix[0].length; ++i) {
				
				//Default for each loop
				makeDefault();
	
				while (!libraryIndex.endoffile() || minDistance != 0) {
					
					double checkDistance = calcDist(cellMatrix[i][j], libraryIndex[i], type);
	
					if (checkDistance < minDistance) {
						
						minPointer = pointer;
						minDistance = checkDistance;
					}
				}
				
				//process mosaic matrix cell
				mosaicMatrix[i][j] = pointer;
			}
		}
	}
	//calculate RGB distance
	double calcDist(Cell a, Cell b, char type) {
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
	}
}
