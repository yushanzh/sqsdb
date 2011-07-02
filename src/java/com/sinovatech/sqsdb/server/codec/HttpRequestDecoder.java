package com.sinovatech.sqsdb.server.codec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.sinovatech.sqsdb.common.PropUtil;

/**
 * A {@link org.apache.mina.filter.codec.demux.MessageDecoder} that decodes {@link HttpRequest}.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class HttpRequestDecoder extends MessageDecoderAdapter {
	private static final Log log = LogFactory.getLog(HttpRequestDecoder.class);
    private static final byte[] CONTENT_LENGTH = "Content-Length:".getBytes();
    public static final byte CR = 0X0D;
    public static final byte LF = 0X0A;
    public static final byte SP = 0X20;
    public static final byte CO = 0X3A;
    public static final byte SEPARATOR = PropUtil.getByte("sqsconfig", "separator.element");

    public static final CharsetDecoder decoder = Charset.forName(PropUtil.getString("sqsconfig","Charset")).newDecoder();

    public HttpRequestDecoder() {
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        try {
//        	log.info("decodable ::::::: "+messageComplete(in));
            return messageComplete(in) ? MessageDecoderResult.OK
                    : MessageDecoderResult.NEED_DATA;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to decode body
//    	long begin = System.currentTimeMillis();
        HttpMessage m = decodeBody(in);

        // Return NEED_DATA if the body is not fully read.
        if (m == null)
            return MessageDecoderResult.NEED_DATA;

        out.write(m);
//        long time = System.currentTimeMillis()-begin;
//        log.debug("decode time === "+time);

        return MessageDecoderResult.OK;
    }
    
    /**
     * HTTP请求验证(严格)
     * 
     * @param in
     * @return
     * @throws Exception
     * @author wanghailong@sinovatech.com -- 2011-4-26
     * @since
     */
    private boolean messageComplete(IoBuffer in,String test) throws Exception {
        int last = in.remaining() - 1;
        if (in.remaining() < 20)
            return false;

        // to speed up things we check if the Http request is a GET or POST
        if (in.get(0) ==  71 && in.get(1) == 69
                && in.get(2) == 84) {
            // Http GET request therefore the last 4 bytes should be 0x0D 0x0A 0x0D 0x0A
            return (in.get(last) == LF
                    && in.get(last - 1) == CR
                    && in.get(last - 2) == LF && in.get(last - 3) == CR);
        } else if (in.get(0) == 80 && in.get(1) == 79
                && in.get(2) == 83 && in.get(3) == 84 || (in.get(0) == 72 && in.get(1) == 84
                        && in.get(2) == 84 && in.get(3) == 80)) {
            // Http POST request
            // first the position of the 0x0D 0x0A 0x0D 0x0A bytes
            int eoh = -1;
            for (int i = last; i > 2; i--) {
                if (in.get(i) == (byte) 0x0A && in.get(i - 1) == (byte) 0x0D
                        && in.get(i - 2) == (byte) 0x0A
                        && in.get(i - 3) == (byte) 0x0D) {
                    eoh = i + 1;
                    break;
                }
            }
            if (eoh == -1)
                return false;
            for (int i = 0; i < last; i++) {
                boolean found = false;
                for (int j = 0; j < CONTENT_LENGTH.length; j++) {
                    if (in.get(i + j) != CONTENT_LENGTH[j]) {
                        found = false;
                        break;
                    }
                    found = true;
                }
                if (found) {
                    // retrieve value from this position till next 0x0D 0x0A
                    StringBuilder contentLength = new StringBuilder();
                    for (int j = i + CONTENT_LENGTH.length; j < last; j++) {
                        if (in.get(j) == 0x0D)
                            break;
                        contentLength.append(new String(
                                new byte[] { in.get(j) }));
                    }
                    // if content-length worth of data has been received then the message is complete
                    return (Integer.parseInt(contentLength.toString().trim())
                            + eoh == in.remaining());
                }
            }
        } 

        // the message is not complete and we need more data
        return false;
    }
    
    /**
     * HTTP请求验证(简单)
     * 
     * @param in
     * @return
     * @throws Exception
     * @author wanghailong@sinovatech.com -- 2011-4-26
     * @since
     */
    private boolean messageComplete(IoBuffer in) throws Exception {
        int last = in.remaining() - 1;
        if (in.remaining() < 20)
            return false;

        // to speed up things we check if the Http request is a GET or POST
        if (in.get(0) == 71 && in.get(1) == 69
                && in.get(2) == 84) {
            // Http GET request therefore the last 4 bytes should be 0x0D 0x0A 0x0D 0x0A
            return (in.get(last) == LF
                    && in.get(last - 1) == CR
                    && in.get(last - 2) == LF && in.get(last - 3) == CR);
        } else if (in.get(0) == 80 && in.get(1) == 79
                && in.get(2) == 83 && in.get(3) == 84 || (in.get(0) == 72 && in.get(1) == 84
                      && in.get(2) == 84 && in.get(3) == 80)) {
        	return true;           
        }

        // the message is not complete and we need more data
        return false;
    }

    private HttpMessage decodeBody(IoBuffer in) {
        
        try {
//        	String a = in.getString(decoder);
//        	log.debug("receiver : "+a.substring(0,100));
        	if(in.get(0) == 72 && in.get(1) == 84
                    && in.get(2) == 84 && in.get(3) == 80){
        		return parseResponse(in);
        	}else{
                return parseRequest(in);
        	}
        } catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    private HttpResponseMessage parseResponse(IoBuffer in) throws CharacterCodingException{
    	int last = in.remaining();
    	HttpResponseMessage response = new HttpResponseMessage();
//    	IoBuffer buf = IoBuffer.allocate(10).setAutoExpand(true);
    	ByteBuffer buf = ByteBuffer.allocate(10);
    	for(int i=9; i<20; i++){
    		if(in.get(i)==SP){
    			buf.flip();
//    			response.setResponseCode(Integer.parseInt(decoder.decode(buf).toString()));//jdk BUG number 5005419
    			response.setResponseCode(Integer.parseInt(new String(buf.array(),0,buf.remaining())));
    			buf.clear();
    			break;
    		}
    		buf.put(in.get(i));
    	}
    	
    	int pos = last-1;
    	for(;pos>20;pos--){
    		if(in.get(pos) == LF){
    			if(in.get(pos-1) == CR && in.get(pos-2) == LF && in.get(pos-3) == CR){
	    			in.position(pos+1);
	    			break;
    			}
    		}
    	}
    	if(pos>20){
    		response.appendBody(in.getString(decoder));
    	}else{
    		return null;
    	}
    	
    	
    	return response;
    }
    
    private HttpRequestMessage parseRequest(IoBuffer in) throws IOException {
    	int last = in.remaining();
    	HttpRequestMessage request = new HttpRequestMessage();
    	IoBuffer buf = IoBuffer.allocate(2048).setAutoExpand(true);
//    	ByteBuffer buf = ByteBuffer.allocate(2048);
    	int pos = 0;
    	byte b ;
    	String key=null;
    	boolean isValue = true;
    	Map map = new HashMap();
    	
    	if (in.get(0) == 71 && in.get(1) == 69
                && in.get(2) == 84) {
    		
            pos = 4;
            for(int i=pos;i<last;i++){
            	
            	b = in.get(i);
        		if(b==SP && isValue){
        			buf.flip();
        			if(key!=null)
        				map.put(key,new String(buf.array(),0,buf.remaining()));
        			buf.clear();
        			break;
        		}
        		if(b==0x3F && isValue){
        			buf.flip();
        			request.setPath(new String(buf.array(),0,buf.remaining()));
        			buf.clear();
        			continue;
        		}
        		if(b==0x3D && isValue){
        			buf.flip();
        			key = new String(buf.array(),0,buf.remaining());
        			buf.clear();
        			continue;
        		}
        		if(b==0x26 && isValue){
        			buf.flip();
        			if(key!=null)
        				map.put(key,new String(buf.array(),0,buf.remaining()));
        			key = null;
        			buf.clear();
        			continue;
        		}
        		if(b==SEPARATOR && SEPARATOR>0){
        			isValue = !isValue;
        			continue;
        		}
        		buf.put(b);
        	}
            
        } else if (in.get(0) == 80 && in.get(1) == 79
                && in.get(2) == 83 && in.get(3) == 84){
        	pos = 5;
        	
        	for(int i=pos;i<last;i++){
	        	b = in.get(i);
	    		if(b==SP){
	    			buf.flip();
	    			request.setPath(new String(buf.array(),0,buf.remaining()));
        			buf.clear();
	    			break;
	    		}
	    		buf.put(b);
        	}
        	
        	pos = last-1;
        	for(;pos>20;pos--){
        		if(in.get(pos) == LF){
        			if(in.get(pos-1) == CR && in.get(pos-2) == LF && in.get(pos-3) == CR){
//    	    			in.position(pos+1);
    	    			break;
        			}
        		}
        	}
        	if(pos>20){
        		
        		for(int i=pos+1;i<last;i++){
                	
                	b = in.get(i);
            		if(b==0x3D && isValue){
            			buf.flip();
            			key = new String(buf.array(),0,buf.remaining());
            			buf.clear();
            			continue;
            		}
            		if(b==0x26 && isValue){
            			buf.flip();
            			if(key!=null)
            				map.put(key,new String(buf.array(),0,buf.remaining()));
            			key = null;
            			buf.clear();
            			continue;
            		}
            		if(b==SEPARATOR && SEPARATOR>0){
            			isValue = !isValue;
            			continue;
            		}
            		buf.put(b);
            	}
        		
    			buf.flip();
    			if(key!=null)
    				map.put(key,new String(buf.array(),0,buf.remaining()));
    			buf.clear();
        			
        		
        	}else{
        		return null;
        	}
        	
        }

		request.setHeaders(map);
		in.position(last);
		return request;


    }
    
 private HttpResponseMessage parseResponse(IoBuffer in, String test) throws CharacterCodingException {
	 HttpResponseMessage response = new HttpResponseMessage();
		Map<String, String> map = new HashMap();
		BufferedReader rdr = new BufferedReader(new StringReader(in.getString(decoder)));

		try {
			String line = rdr.readLine();
			String[] url = line.split(" ");
			map.put("Protocol", url[0]);
			response.setResponseCode(Integer.parseInt(url[1]));
			map.put("Context", url[2]);

			// Read header
			while ((line = rdr.readLine()) != null && line.length() > 0) {
				String[] tokens = line.split(": ");
				map.put(tokens[0], tokens[1]);
			}

			if (map.get("Content-Length") != null
					&& !"0".equals(map.get("Content-Length"))) {
				int len = Integer.parseInt(map.get("Content-Length"));
				char[] buf = new char[len];
				if (rdr.read(buf) == len) {
					line = String.copyValueOf(buf);
				}
				response.appendBody(line);
			}
			response.setHeaders(map);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response;
	}

 private HttpRequestMessage parseRequest(IoBuffer in, String test) throws CharacterCodingException {
	 	HttpRequestMessage request = new HttpRequestMessage();
        Map<String, String> map = new HashMap();
        BufferedReader rdr = new BufferedReader(new StringReader(in.getString(decoder)));
        String line = null;

        try {
            // Get request URL.
            line = rdr.readLine();
            String[] url = line.split(" ");
//            if (url.length < 3)
//                return map;

//            map.put("URI",  line);
            map.put("Method",  url[0].toUpperCase());
            request.setPath(url[1]);
//            map.put("Context", url[1].substring(1));
//            map.put("Protocol", url[2]);
            // Read header
            while ((line = rdr.readLine()) != null && line.length() > 0) {
                String[] tokens = line.split(": ");
                map.put(tokens[0], tokens[1]);
            }

            // If method 'POST' then read Content-Length worth of data
            if (url[0].equalsIgnoreCase("POST")) {
                int len = Integer.parseInt(map.get("Content-Length"));
                char[] buf = new char[len];
                if (rdr.read(buf) == len) {
                    line = String.copyValueOf(buf);
                }
            } else if (url[0].equalsIgnoreCase("GET")) {
                int idx = url[1].indexOf('?');
                if (idx != -1) {
                	request.setPath(url[1].substring(0, idx));
//                    map.put("Context",
//                            url[1].substring(1, idx));
                    line = url[1].substring(idx + 1);
                } else {
                    line = null;
                }
            }
            if (line != null) {
                String[] match = line.split("\\&");
                for (int i = 0; i < match.length; i++) {
                    String[] params = new String[1];
                    String[] tokens = match[i].split("=");
                    switch (tokens.length) {
                    case 0:
                        map.put(match[i], "");
                        break;
                    case 1:
                        map.put(tokens[0], "");
                        break;
                    default:
                        String name = tokens[0];
//                        if (map.containsKey(name)) {
//                            params = map.get(name);
//                            String[] tmp = new String[params.length + 1];
//                            for (int j = 0; j < params.length; j++)
//                                tmp[j] = params[j];
//                            params = null;
//                            params = tmp;
//                        }
//                        params[params.length - 1] = tokens[1].trim();
//                        map.put(name, params);
                    	  map.put(name, tokens[1]);
                    }
                }
            }
            request.setHeaders(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return request;
    }
}
