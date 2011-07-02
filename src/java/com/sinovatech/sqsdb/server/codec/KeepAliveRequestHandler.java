package com.sinovatech.sqsdb.server.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

public class KeepAliveRequestHandler implements KeepAliveRequestTimeoutHandler {
	private static final Log log = LogFactory.getLog(KeepAliveRequestHandler.class);
	
	public void keepAliveRequestTimedOut(KeepAliveFilter paramKeepAliveFilter,
			IoSession paramIoSession) throws Exception {
		log.info("keepAliveRequestTimedOut to close ...");
		paramIoSession.close(true);
		paramIoSession.getService().dispose();
	}

}
