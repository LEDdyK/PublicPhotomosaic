package main.images.test;

import org.junit.Test;

import main.images.downloader.ImageDownloader;

public class ImageDownloaderTest {
	
	@Test
	public void testDownloadRecentPhotos() {
		new ImageDownloader().downloadRecentImages(1);
	}

}
