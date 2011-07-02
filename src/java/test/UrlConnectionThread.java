package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class UrlConnectionThread extends Thread {
	static Logger logger = Logger.getLogger(UrlConnectionThread.class);
	int count = 1;
	String ip = "127.0.0.1:8080";
	String content = "";
	
	public UrlConnectionThread(int count, String ip, String content) {
		super();
		this.count = count;
		this.ip = ip;
		this.content = content;
	}

	public void run() {
		
		try {
			
			for (int i = 0; i < count; i++) {
				URL url = new URL("http://" + ip + "/put?" + content+(Test.l++));
				logger.info("http://" + ip + "/put?" + content+(Test.l++));
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				logger.info(conn.getContentType());
				logger.info(conn.getContentEncoding());
				logger.info(conn.getResponseCode());
				
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String lines = null;
				while ((lines = reader.readLine()) != null) {
					logger.info("body :::::: "+lines);
				}
				reader.close();
				conn.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
