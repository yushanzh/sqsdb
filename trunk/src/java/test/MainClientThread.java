package test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.sinovatech.sqsdb.client.HttpClientSupport;
import com.sinovatech.sqsdb.client.RoundRobin;
import com.sinovatech.sqsdb.server.codec.HttpRequestMessage;
import com.sinovatech.sqsdb.server.codec.HttpResponseMessage;
import com.sinovatech.sqsdb.socket.TCPClient;

public class MainClientThread extends Thread {
	static Logger logger = Logger.getLogger(MainClientThread.class);
	int count = 1000;
	static AtomicInteger i=new AtomicInteger();
//	public static AtomicLong time=new AtomicLong(0);
	String ip = "127.0.0.1:8080";
	String path = "";
	String content = "";
	private CountDownLatch begin; 
	private CountDownLatch end;
	private CountDownLatch testend;
	int maxctive = 0;
	
	public MainClientThread(int count, String ip, String path, String content,CountDownLatch begin, CountDownLatch end, CountDownLatch testend,int maxctive) {
		super();
		this.count = count;
		this.path = path;
		this.ip = ip;
		this.content = content;
		this.begin = begin;   
		this.end = end; 
		this.testend = testend;
		this.maxctive = maxctive;
	}

	public void run() {
		try {
//			long beg = System.currentTimeMillis();
//			RoundRobin rr = new RoundRobin();
			HttpClientSupport client = new HttpClientSupport();
//			int code= 1;
//			client.getIoSession(code);
			
			
			
			for (; i.get() < count; ){
				i.incrementAndGet();
				
				
//				synchronized(i){
//					i.incrementAndGet();
//					testend.countDown();
//					
//					if(i.get() > count)break;
//					if(i.get() > maxctive){
//						begin.await();
//					}
//				}
//				end.countDown();
				
				HttpRequestMessage request = new HttpRequestMessage(path, content+(Test.l++));
//				HttpRequestMessage request = new HttpRequestMessage("/put","test",null,"sk1=123456789&sk2=123456&sk3=15120189784&content=english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000english0000000&pk=9999");
				IoSession c = client.getIoSession(ip);
				WriteFuture future = c.write(request);
//				WriteFuture future = c.write("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
				future.await();
				if(path.equals("/poll")){
					ReadFuture readFuture = c.read();
					readFuture.await();
				}
				
				
				
//				if(i==5000) {this.sleep(5000);tt=System.currentTimeMillis();logger.info("tt===================="+tt);}
//				logger.info("======================"+future.isWritten());
//				ReadFuture readFuture = c.read();
//				if(readFuture.await(5000, TimeUnit.MILLISECONDS)){
//					logger.info("======================"+((HttpResponseMessage)readFuture.getMessage()).getBody());
//				}
				
				
				
			}
//			time.addAndGet(System.currentTimeMillis()-beg);
			end.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
