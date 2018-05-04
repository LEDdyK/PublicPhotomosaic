package main.images.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ImageDownloader {
	
	public static final String API_KEY = "&api_key=89290e67c97452eacabe885466495603";
	public static final String REST_FORMAT = "&format=rest";
	public static final String SECRET = "6fea535f82bba7de";
	
	public static final String BASE_URL = "https://api.flickr.com/services/rest/?method=";
	public static final String RECENT_IMAGES = BASE_URL + "flickr.photos.getRecent";
	
	public static final String DOWNLOAD_URL = "https://farm%d.staticflickr.com/%d/%s_%s_%s.%s";
	
	
	public void downloadRecentImages() {
		try {
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
			
			System.out.println(content.toString());
			
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			factory.setNamespaceAware(true);
//			factory.setValidating(true);
//			factory.setXIncludeAware(true);
//			
//			DocumentBuilder parser = factory.newDocumentBuilder();
//			StringReader reader = new StringReader(content.toString());
//			InputSource ins = new InputSource(reader);
//			Document document = parser.parse(ins);
//			
//			System.out.println(document.toString());
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private List<PhotoMetaData> parseXMLResult(String xmlContent) {
		List<PhotoMetaData> parsedResults = new ArrayList<>();
		
		Scanner scanner = new Scanner(xmlContent);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			if (line.contains("photo id=")) {
				
			}
		}
		
		return parsedResults;
		
	}
	
	

}
