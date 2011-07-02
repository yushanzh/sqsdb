package com.sinovatech.sqsdb.socket;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;

import com.sinovatech.sqsdb.common.PropUtil;

/**
 * IoHandler对象工厂
 * @author Administrator
 *
 */
public class HandlerFactory {
	
	static Logger logger = Logger.getLogger(HandlerFactory.class.getName());
	
	private static final String MINASOCKET = "minasocket";
	/**
	 * 根据类名返回,该对象实例
	 * @param className 类名
	 * @return
	 * @author soap (Nov 25, 2009)
	 */
	public static IoHandler getIoHandler(String name) {
		String className = PropUtil.getString(MINASOCKET,name);
		IoHandler handle = null;
		try {
			logger.debug("HandlerFactory newInstance:"+className);
			handle = (IoHandler) Class.forName(className).newInstance();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(e);
		}
		return handle;
	}	
}
