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


/**
 * A HTTP response message.
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class HttpResponseMessage extends HttpMessage{
    /** HTTP response codes */
    public static final int HTTP_STATUS_SUCCESS = 200;

    public static final int HTTP_STATUS_NOT_FOUND = 404;
    
    public static final int HTTP_STATUS_SERVER_ERROR = 500;

    private int responseCode = HTTP_STATUS_SUCCESS;
    
    

    public HttpResponseMessage() {
//    	headers = new HashMap();
//    	headers.put("Server", "HttpServer (sinovatech.com) V0.1");
//        headers.put("Cache-Control", "private");
//        headers.put("Content-Type", "text/html; charset="+charset);
//        headers.put("Connection", "keep-alive");
//        headers.put("Date", sf.format(new Date()));
    }


    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

}
