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



import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.server.common.IHandler;
import com.sinovatech.sqsdb.server.common.SqsHandlerFactory;




/**
 * An {@link IoHandler} for HTTP.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class ServerHandler extends IoHandlerAdapter {
	private static final Log log = LogFactory.getLog(ServerHandler.class);
	private static final IHandler handler = SqsHandlerFactory.getIFlowHandler("sqs.handler");

    @Override
    public void messageReceived(IoSession session, Object message) {
    	
//    	long begin = System.currentTimeMillis();
    	String result = "";
    	HttpResponseMessage response = new HttpResponseMessage();
		
    	if(message instanceof HttpRequestMessage){//http
    		
    		HttpRequestMessage request = (HttpRequestMessage)message;
    		log.debug("request::::::"+request);
    		String context = request.getContext();
    		try {
				if (context.length()>0) {
					
					Method method = handler.getClass().getMethod(
							context,
							new Class[] { HttpRequestMessage.class });
					result = (String) method.invoke(handler, request);
				}
				log.debug("result : "+result);
			} catch (NoSuchMethodException e) {
				log.error("NoSuchURL : "+context, e);
				response.appendBody("NoSuchURL : "+context);
				response.setResponseCode(HttpResponseMessage.HTTP_STATUS_NOT_FOUND);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				response.appendBody(e.getMessage());
				response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SERVER_ERROR);
			} 
			
			response.appendBody(result);
			session.write(response);
        
    	}else{
    		log.info("http protocol error");
    	}

//    	log.debug("serverhandler time === "+(System.currentTimeMillis()-begin));
        
    }


	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
		session.close(false);
		log.error("exceptionCaught : ",cause);
	}


	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
//		session.setIdleTime(IdleStatus.BOTH_IDLE, PropUtil.getInt("minasocket","socket.maxIdleTime")*2);
	}


	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
//		session.close(true);
		log.info("server maxIdleTime timeout £º" + PropUtil.getInt("minasocket","socket.maxIdleTime")*2);
	}


}
