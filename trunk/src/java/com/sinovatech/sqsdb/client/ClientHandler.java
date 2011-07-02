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
package com.sinovatech.sqsdb.client;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.server.codec.HttpResponseMessage;




/**
 * 
 * ClientHandler客户端处理入口.
 * <p>
 * 客户端处理入口,IoHandler的实现.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-4-25
 */
public class ClientHandler extends IoHandlerAdapter {
	private static final Log log = LogFactory.getLog(ClientHandler.class);
	HttpClientSupport clients = new HttpClientSupport();

    /**
     * 消息接入事件处理
     */
	@Override
    public void messageReceived(IoSession session, Object message) throws Exception{
    	String result = "";
    	if(message instanceof HttpResponseMessage){
    		
    		HttpResponseMessage response = (HttpResponseMessage)message;
    		log.info("body::::::"+response.getBody());
    		clients.returnIoSession(session.getServiceAddress().toString().substring(1), session);

        
    	}
        
    }

	/**
     * 异常事件处理
     */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
		session.close(false);
		log.error("exceptionCaught : ",cause);
	}

	/**
     * 连接创建事件处理
     */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
//		session.setIdleTime(IdleStatus.BOTH_IDLE, PropUtil.getInt("minasocket","socket.maxIdleTime"));
	}

	/**
     * 连接空闲事件处理
     */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
//		todo心跳功能KeepAliveMessageFactory
		super.sessionIdle(session, status);
		log.info("client maxIdleTime timeout ：" + PropUtil.getInt("minasocket","socket.maxIdleTime")+" exec beat...");
		session.close(true);
		
	}


}
