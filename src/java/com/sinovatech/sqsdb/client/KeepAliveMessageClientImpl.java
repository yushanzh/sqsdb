package com.sinovatech.sqsdb.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.sinovatech.sqsdb.server.codec.HttpRequestMessage;
import com.sinovatech.sqsdb.server.codec.HttpResponseMessage;

public class KeepAliveMessageClientImpl implements KeepAliveMessageFactory {
	private static final Log log = LogFactory.getLog(KeepAliveMessageClientImpl.class);
	private static final HttpRequestMessage KAMSG_REQ = new HttpRequestMessage("/heartbeatrequest","");  
//	private static final HttpResponseMessage KAMSG_RES = new HttpResponseMessage();
//	static{
//		KAMSG_RES.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
//		KAMSG_RES.appendBody("heartbeatresponse");
//	}
	private static final String KAMSG_RES = "heartbeatresponse";
	
	public Object getRequest(IoSession paramIoSession) {
		log.info("getRequest......");
		return KAMSG_REQ;
	}

	public Object getResponse(IoSession paramIoSession, Object paramObject) {
		return null;
	}

	public boolean isRequest(IoSession paramIoSession, Object paramObject) {
		if(paramObject instanceof HttpRequestMessage){
			return KAMSG_REQ.getPath().equals(((HttpRequestMessage)paramObject).getPath());
		}else{
			return KAMSG_RES.equals(((HttpResponseMessage)paramObject).getBody());
		}
	}

	public boolean isResponse(IoSession paramIoSession, Object paramObject) {
		if(paramObject instanceof HttpRequestMessage){
			return KAMSG_REQ.getPath().equals(((HttpRequestMessage)paramObject).getPath());
		}else{
			return KAMSG_RES.equals(((HttpResponseMessage)paramObject).getBody());
		}
	}

}
