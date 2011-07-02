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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;

import com.sinovatech.sqsdb.common.ObjectFactory;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.socket.HandlerFactory;
import com.sinovatech.sqsdb.socket.Server;
import com.sinovatech.sqsdb.socket.TCPServer;


/**
 * (<b>Entry point</b>) HTTP server
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class HttpServer {
	private static final Log log = LogFactory.getLog(HttpServer.class);
	private static Server server = null;
	
	public static void start() throws IOException{
		server = new TCPServer(HandlerFactory.getIoHandler("server.handler"));
		
//		server.addLast("protocolFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
		server.addLast("protocolFilter",new ProtocolCodecFilter((ProtocolCodecFactory)ObjectFactory.getInstance(PropUtil.getString("sqsconfig", "protocol.factory"))));
//		server.addLogLast();
		
		if(PropUtil.getString("minasocket","socket.KeepAlive.Impl")!=null && !"".equals(PropUtil.getString("minasocket","socket.KeepAlive.Impl"))){
			KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageServerImpl(), IdleStatus.BOTH_IDLE);
			keepAliveFilter.setForwardEvent(false);  
			keepAliveFilter.setRequestInterval(PropUtil.getInt("minasocket","socket.maxIdleTime")*2);//自动校正IdleStatus的idleTime
			keepAliveFilter.setRequestTimeout(PropUtil.getInt("minasocket","socket.maxIdleTime"));
			keepAliveFilter.setRequestTimeoutHandler(new KeepAliveRequestHandler());
			server.addLast("heartbeat", keepAliveFilter);
		}
		
		
		server.start();
		log.info("Server start succeed!");
	}
	
	public static void shutdown(){
		server.shutdown();
		log.info("Server shutdown succeed!");
	}

    public static void main(String[] args) {
    	
    	
        try {
        	HttpServer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
