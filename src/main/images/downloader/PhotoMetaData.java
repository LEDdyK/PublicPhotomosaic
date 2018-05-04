package main.images.downloader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoMetaData {
	private String id;
	private String secret;
	private int server;
	private int farm;
	
	public PhotoMetaData(String xmlLine) {
		Pattern pattern = Pattern.compile("\"([^\"]*)\"");
		Matcher m = pattern.matcher(xmlLine);
		
		m.find();
		id = m.group(0).substring(1, m.group().length() - 1);
		m.find();
		m.find();
		secret = m.group(0).substring(1, m.group().length() - 1);;
		m.find();
		server = Integer.parseInt(m.group(0).substring(1, m.group().length() - 1));
		m.find();
		farm = Integer.parseInt(m.group(0).substring(1, m.group().length() - 1));
	}

	public String getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}

	public int getServer() {
		return server;
	}

	public int getFarm() {
		return farm;
	}


}
