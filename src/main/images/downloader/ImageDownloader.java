package main.images.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.TaskInfoType;
import main.gui.JFXGui;
import pt.runtime.WorkerThread;
import pu.loopScheduler.LoopRange;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;

public class ImageDownloader {
	
	public static final String API_KEY = "&api_key=89290e67c97452eacabe885466495603";
	public static final String REST_FORMAT = "&format=rest";
	public static final String SECRET = "6fea535f82bba7de";
	
	public static final String BASE_URL = "https://api.flickr.com/services/rest/?method=";
	public static final String RECENT_IMAGES = BASE_URL + "flickr.photos.getRecent";
	
	public static final String DOWNLOAD_URL = "https://farm%d.staticflickr.com/%d/%s_%s_%s.%s";
	
	public static double downCount;
	
	@Future
	public Void[] futureGroup = new Void[1];
	
	public Void downloadRecentImages(int numOfThreads) {
		try {
			
			String xmlResult = getRecentPhotosMetaDataXML();
			List<PhotoMetaData> photoMetaDataList = parseXMLResult(xmlResult);
			new File("photos").mkdir();

			LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, photoMetaDataList.size(), 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
			downCount = 0;
			@Future(taskType = TaskInfoType.MULTI)
			Void task = downloadImages(scheduler, photoMetaDataList);
			futureGroup[0] = task;
			

			//downloadImages(photoMetaDataList);
		
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public void waitTillFinished() {
		Void barrier = futureGroup[0];
	}
	
	public Void downloadImages(LoopScheduler scheduler, List<PhotoMetaData> list) {
		WorkerThread worker = (WorkerThread) Thread.currentThread();
		LoopRange range = scheduler.getChunk(worker.getThreadID());
		
		if (range != null) {
			for (int i = range.loopStart; i < range.loopEnd; i++) {
				PhotoMetaData photoMetaData = list.get(i);
				
				String link = String.format(DOWNLOAD_URL, 
						photoMetaData.getFarm(),
						photoMetaData.getServer(),
						photoMetaData.getId(),
						photoMetaData.getSecret(),
						"q", "jpg");
				
				System.out.println(worker.getThreadID() + ": " + link);
				downloadImage(link, photoMetaData);
				@Gui
				Void progress = updateDownProgress();
			}
		}
		
		return null;
		
	}
	
	public void downloadImages(List<PhotoMetaData> list) {
		for (int i = 0; i < list.size(); i++) {
			PhotoMetaData photoMetaData = list.get(i);
			
			String link = String.format(DOWNLOAD_URL, 
					photoMetaData.getFarm(),
					photoMetaData.getServer(),
					photoMetaData.getId(),
					photoMetaData.getSecret(),
					"q", "jpg");
			
			System.out.println(link);
			downloadImage(link, photoMetaData);
		}
	}
	
	/**
	 * Downloads the image from the provided url using the photoMetaData to determine
	 * the file name
	 * @param url
	 * @param photoMetaData
	 * @throws IOException
	 */
	private void downloadImage(String url, PhotoMetaData photoMetaData) {
		try(InputStream in = new URL(url).openStream()){
		    Files.copy(in, Paths.get(System.getProperty("user.dir") + "/photos/" + photoMetaData.getImageName()));
		} catch (IOException e) {
			System.out.println("Image already exists");
		}
	}
	
	/**
	 * Makes an API request to the Flickr api to retrieve an XML of 
	 * the metadata of the most recent photos uploaded
	 * @return a String representing the XML file
	 * @throws IOException
	 */
	private String getRecentPhotosMetaDataXML() throws IOException {
		URL url = new URL(RECENT_IMAGES + API_KEY + REST_FORMAT);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer content = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine + "\n");
		}
		
		in.close();
		
		return content.toString();
	}
	
	/**
	 * Parses an XML String to create a List of PhotoMetaData objects
	 * @param xmlContent
	 * @return
	 */
	private List<PhotoMetaData> parseXMLResult(String xmlContent) {
		List<PhotoMetaData> parsedResults = new ArrayList<>();
		
		Scanner scanner = new Scanner(xmlContent);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			if (line.contains("photo id=")) {
				parsedResults.add(new PhotoMetaData(line));
			}
		}
		
		return parsedResults;
		
	}
	
	private Void updateDownProgress() {
		++downCount;
		JFXGui.downProp.setCount(downCount);
		return null;
	}

}
