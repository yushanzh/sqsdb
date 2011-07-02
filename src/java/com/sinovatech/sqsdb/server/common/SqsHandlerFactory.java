package com.sinovatech.sqsdb.server.common;

import org.apache.log4j.Logger;

import com.sinovatech.sqsdb.common.PropUtil;


public class SqsHandlerFactory {
	static Logger logger = Logger.getLogger(SqsHandlerFactory.class.getName());
	
	private static final String SQSCONFIG = "sqsconfig";
	
	public static IHandler getIFlowHandler(String name) {
		String className = PropUtil.getString(SQSCONFIG,name);
		IHandler handle = null;
		try {
			logger.info("SqsHandlerFactory newInstance:"+className);
			handle = (IHandler) Class.forName(className).newInstance();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(e);
		}
		return handle;
	}
}
