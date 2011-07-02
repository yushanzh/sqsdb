package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.sqsdb.client.HttpClientSupport;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.server.codec.HttpRequestMessage;
import com.sinovatech.sqsdb.server.common.SqsHandlerFactory;
import com.sinovatech.sqsdb.socket.TCPClient;

public class Test {
	static Logger logger = Logger.getLogger(Test.class.getName());
	public static long l = 0L;
	static Map<Object, Integer> taskmap =  new ConcurrentHashMap<Object, Integer>(); 

public static void main(String[] args) throws Exception{
//	IoBuffer buf = IoBuffer.allocate(10);
	
//	CharsetDecoder decoder = Charset.forName("gbk").newDecoder();
//			
//	ByteBuffer buf = ByteBuffer.allocate(10);
//	buf.put((byte)'a');
//	buf.put((byte)'b');
//	
//	buf.flip();
//	System.out.println(buf.remaining());
//	System.out.println(buf.limit());
//	System.out.println(buf.capacity());
//	System.out.println(buf.position());

//	System.out.println(decoder.decode(buf).toString());
//	System.out.println(new String(buf.array(),0,buf.remaining()));
//	
//	System.out.println(new String(buf.array()));System.out.println(new String(buf.array()).length());
//	
//	if(true)return;

//	ReentrantLock lock = new ReentrantLock();
   	String ip = GlobalConfig.getProperty("test", "ip");
   	int count = GlobalConfig.getIntegerProperty("test", "thread.count");
   	String content = GlobalConfig.getProperty("test", "content");
   	String path = GlobalConfig.getProperty("test", "path");
   	int threadNum = GlobalConfig.getIntegerProperty("test", "thread.num");
   	int maxctive = PropUtil.getInt("minasocket","client.maxactive");
   	
   	CountDownLatch testend = new CountDownLatch(maxctive);
   	CountDownLatch end = new CountDownLatch(1); 
   	CountDownLatch begin = new CountDownLatch(1);

   	
   	long beg = System.currentTimeMillis();
//   	"".indexOf("");
   	Thread c = null;
   	for(int i=0;i<threadNum;i++){
//	   	c = new HttpClientThread(count,ip,content);
	   	c = new MainClientThread(count,ip,path,content,begin,end,testend,maxctive);
//	   	c = new UrlConnectionThread(count,ip,content);
	   	c.start();//预试开发始
//	   	c.join();
   	}
   	
//   	try {
//   			testend.await();//预试结束
   			
//   			Thread.currentThread().sleep(5000);//准备正式开始
   			
   			
//   			logger.info("begin===================="+(System.currentTimeMillis()-t));
//   		   	t = System.currentTimeMillis();
//   			begin.countDown();//正试开始
   			
   			
//			end.await();// 结束
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} finally {
//			logger.info("结束");
//		}
   	
   	end.await();
   	logger.info("real===================="+(System.currentTimeMillis()-beg));
//   	logger.info("real===================="+(now-MainClientThread.tt));

    }
}
