package main.images;

public class ImageRGB {
	private int red;
	private int green;
	private int blue;
	private String fileName;
	
	public ImageRGB(int r, int g, int b, String name){
		red = r;
		green = g;
		blue = b;
		fileName = name;
	}

	public int getRed() {
		return red;
	}
	public int getGreen() {
		return green;
	}
	public int getBlue() {
		return blue;
	}
	public String getName() {
		return fileName;
	}
}
