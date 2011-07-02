package com.sinovatech.sqsdb.server.codec;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import com.sinovatech.sqsdb.common.PropUtil;

public abstract class HttpMessage implements Message {
	    
	/** Map<String, String> */
	Map<String,String> headers = null;
	
	/** Storage for body of HTTP */
	String body = "";
	
	public static String charset = PropUtil.getString("sqsconfig", "Charset");
	
	
	public void putHead(String type,String value){
		headers.put(type, value);
	}
	
	public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public Map getHeaders() {
        return headers;
    }
    
    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }
    
    

    public void appendBody(String s) {
    	body += s;
    }
    
    public String getBody() {
        return body;
    }

    public int getBodyLength() {
        try {
			return body.getBytes(charset).length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    

    public String getParameter(String name) {
        String param = (String) headers.get(name);
        return param == null ? null : param;
    }

    
    public String getHeader(String name) {
        return (String) headers.get(name);
    }
    
    public String toString(){
    	return headers==null?this.getClass().getName():this.headers.toString();
    }
    
//    public String toString() {
//        StringBuilder str = new StringBuilder();
//
//        if(headers != null){
//	        Iterator it = headers.entrySet().iterator();
//	        while (it.hasNext()) {
//	            Entry e = (Entry) it.next();
//	            str.append(e.getKey() + " : "
//	                    + e.getValue() + "\n");
//	        }
//        }
//        return str.toString();
//    }
//    
//    public static String arrayToString(String[] s, char sep) {
//        if (s == null || s.length == 0)
//            return "";
//        StringBuffer buf = new StringBuffer();
//        if (s != null) {
//            for (int i = 0; i < s.length; i++) {
//                if (i > 0)
//                    buf.append(sep);
//                buf.append(s[i]);
//            }
//        }
//        return buf.toString();
//    }
    
    
    
}
