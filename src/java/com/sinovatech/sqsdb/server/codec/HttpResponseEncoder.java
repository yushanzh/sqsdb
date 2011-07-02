/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.sinovatech.sqsdb.server.codec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.sinovatech.sqsdb.common.PropUtil;

/**
 * A {@link MessageEncoder} that encodes {@link HttpResponseMessage}.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class HttpResponseEncoder implements MessageEncoder {

	private static final Log log = LogFactory.getLog(HttpResponseEncoder.class);
	private static final String strCharset = PropUtil.getString("sqsconfig","Charset");
	private static final Charset charset = Charset.forName(strCharset);
    private static final byte[] CRLF = new byte[] { 0x0D, 0x0A };
    private static final byte SP = 0x20;
    private static final byte WE = 0x3F;
    private static final byte CO = 0x3A;
    private static byte[] SERVER = null;
    private static byte[] CACHE_CONTROL =  null;
    private static byte[] CONTENT_TYPE = null;
    private static byte[] CONNECTION = null;
    private static byte[] DATE = null;
    private static byte[] HTTP = null;
    private static byte[] OK = null;
    private static byte[] NOTFOUND = null;
    private static byte[] SERVERERROR = null;
    private static byte[] ACCEPT = null;
    private static byte[] USER_AGENT = null;
    private static byte[] HOST = null;
    private static byte[] Content_Length = null;
    
    public static CharsetEncoder encoder = charset.newEncoder();
    
    private static final SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
    
    static{
    	sf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    	
        try {
			SERVER = "Server: HttpServer(sinovatech.com)V0.1".getBytes(strCharset);
			CACHE_CONTROL = "Cache-Control: private".getBytes(strCharset);
			CONTENT_TYPE = ("Content-Type: text/html; charset="+strCharset).getBytes(strCharset);
			CONNECTION = "Connection: Keep-Alive".getBytes(strCharset);
			DATE = "Date: ".getBytes(strCharset);
			HTTP = "HTTP/1.1".getBytes(strCharset);
			OK = " OK".getBytes(strCharset);
			NOTFOUND = " Not Found".getBytes(strCharset);
			SERVERERROR = "Internal Server Error".getBytes(strCharset);
			ACCEPT = "Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*".getBytes(strCharset);
			USER_AGENT = "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)".getBytes(strCharset);
			HOST = "Host: 127.0.0.1".getBytes(strCharset);
			Content_Length = "Content-Length: ".getBytes(strCharset);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

    public HttpResponseEncoder() {
    	
    	
    }
    

    public void encode(IoSession session, Object message,
            ProtocolEncoderOutput out) throws Exception {
    	HttpMessage msg = (HttpMessage) message;
    	IoBuffer buf = IoBuffer.allocate(2048).setAutoExpand(true);
//    	ByteBuffer buf = ByteBuffer.allocate(2048);
		
		if(msg instanceof HttpResponseMessage){
			HttpResponseMessage response = (HttpResponseMessage)msg;
			
			buf.put(HTTP);
			buf.put(SP);
			buf.put(String.valueOf(response.getResponseCode()).getBytes(strCharset));
			switch (response.getResponseCode()) {
			case HttpResponseMessage.HTTP_STATUS_SUCCESS:
				buf.put(OK);
				break;
			case HttpResponseMessage.HTTP_STATUS_NOT_FOUND:
				buf.put(NOTFOUND);
				break;
			case HttpResponseMessage.HTTP_STATUS_SERVER_ERROR:
				buf.put(SERVERERROR);
				break;
			}
			buf.put(CRLF);
			
			
	        buf.put(SERVER);
	        buf.put(CRLF);
	        buf.put(CACHE_CONTROL);
	        buf.put(CRLF);
	        buf.put(CONTENT_TYPE);
	        buf.put(CRLF);
	        buf.put(CONNECTION);
	        buf.put(CRLF);
	        buf.put(DATE);
//	        buf.putString(sf.format(System.currentTimeMillis()),encoder);//jdk BUG number 5005419
	        buf.put(sf.format(System.currentTimeMillis()).getBytes(strCharset));
	        buf.put(CRLF);
	        
			
		}else if(msg instanceof HttpRequestMessage){
			HttpRequestMessage request = (HttpRequestMessage)msg;
			
			buf.put(request.getRequestMethod().getBytes(strCharset));
			if(HttpRequestMessage.HTTP_METHOD_POST.equalsIgnoreCase(request.getRequestMethod())){
				buf.put(SP);
				buf.put(request.getPath().getBytes(strCharset));
				request.body=request.getParams();
			}else{
				buf.put(SP);
				buf.put(request.getPath().getBytes(strCharset));
				buf.put(WE);
				buf.put(request.getParams().getBytes(strCharset));
			}
			
			buf.put(SP);
			buf.put(HTTP);
			buf.put(CRLF);	
			
	        buf.put(ACCEPT);
	        buf.put(CRLF);
	        buf.put(USER_AGENT);
	        buf.put(CRLF);
	        buf.put(HOST);
	        buf.put(CRLF);
	        buf.put(CONNECTION);
	        buf.put(CRLF);
		}

		if(msg.getHeaders()!=null){
			for (Iterator it = msg.getHeaders().entrySet().iterator(); it.hasNext();) {
				Entry entry = (Entry) it.next();
				buf.put(((String) entry.getKey()).getBytes(strCharset));
				buf.put(CO);
				buf.put(SP);
				buf.put(((String) entry.getValue()).getBytes(strCharset));
				buf.put(CRLF);
			}
		}
		// now the content length is the body length
		if(msg.getBodyLength()>0){
			buf.put(Content_Length);
			buf.put(String.valueOf(msg.getBodyLength()).getBytes(strCharset));
		}
		
		buf.put(CRLF);
		buf.put(CRLF);
		// add body
		if(msg.getBodyLength()>0){
			buf.put(msg.getBody().getBytes(strCharset));
		}

		buf.flip();
//		log.debug(buf.asReadOnlyBuffer().getString(Charset.forName(PropUtil.getString("sqsconfig","Charset")).newDecoder()));
		out.write(buf);
    }

	

}
