package com.sinovatech.sqsdb.server.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class KeepAliveMessageServerImpl implements KeepAliveMessageFactory {
	private static final Log log = LogFactory.getLog(KeepAliveMessageServerImpl.class);
//	private static final HttpRequestMessage KAMSG_REQ = new HttpRequestMessage(HttpRequestMessage.HTTP_METHOD_GET,"heartbeatrequest","");  
	private static final String KAMSG_REQ = "/heartbeatrequest";
//	private static final String KAMSG_RES = "heartbeatresponse";
	private static final HttpResponseMessage KAMSG_RES = new HttpResponseMessage();
	static{
		KAMSG_RES.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		KAMSG_RES.appendBody("heartbeatresponse");
	}
	
	public Object getRequest(IoSession paramIoSession) {
		return null;
	}

	public Object getResponse(IoSession paramIoSession, Object paramObject) {
		log.info("getResponse......");
		return KAMSG_RES;
	}

	public boolean isRequest(IoSession paramIoSession, Object paramObject) {
		if(paramObject instanceof HttpRequestMessage){
			return KAMSG_REQ.equals(((HttpRequestMessage)paramObject).getPath());
		}else{
			return KAMSG_RES.getBody().equals(((HttpResponseMessage)paramObject).getBody());
		}
	}

	public boolean isResponse(IoSession paramIoSession, Object paramObject) {
		if(paramObject instanceof HttpRequestMessage){
			return KAMSG_REQ.equals(((HttpRequestMessage)paramObject).getPath());
		}else{
			return KAMSG_RES.getBody().equals(((HttpResponseMessage)paramObject).getBody());
		}
	}

}
