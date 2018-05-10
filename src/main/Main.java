package main;

import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;

public class Main {

	public static void main(String[] args) {
		new ImageDownloader().downloadRecentImages();
		
		ImageLibrary imglib = new ImageLibrary("photos");
	}
}
