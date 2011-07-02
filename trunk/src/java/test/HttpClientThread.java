package test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpClientThread extends Thread {
	int count = 1;
	String ip = "127.0.0.1:8080";
	String content = "";
	
	public HttpClientThread(int count, String ip, String content) {
		super();
		this.count = count;
		this.ip = ip;
		this.content = content;
	}

	public void run() {
		
		try {
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+ip+ "/put?"+content);
			for (int i = 0; i < count; i++) {
				client.executeMethod(method);
				System.out.println(method.getResponseBodyAsString());
			}
			method.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
