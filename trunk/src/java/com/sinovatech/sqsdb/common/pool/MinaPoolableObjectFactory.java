package com.sinovatech.sqsdb.common.pool;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

import com.sinovatech.sqsdb.socket.HandlerFactory;
import com.sinovatech.sqsdb.socket.TCPClient;


public class MinaPoolableObjectFactory implements KeyedPoolableObjectFactory {
	private static final Log log = LogFactory.getLog(MinaPoolableObjectFactory.class);
   private static final IoHandler ioHandler = HandlerFactory.getIoHandler("client.handler");
	
	public void activateObject(Object arg0, Object arg1) throws Exception {
	}

	public void destroyObject(Object arg0, Object arg1) throws Exception {
		if(arg1 instanceof IoSession) {
			log.info("destroyObject==============");
			IoSession client = (IoSession) arg1;
			client.close(false);
			client.getService().dispose();
        }
		
	}

	public Object makeObject(Object arg0) throws Exception {
		String[] add =  ((String)arg0).split(":");
		TCPClient client = new TCPClient(add[0],Integer.parseInt(add[1]),ioHandler);
		IoSession session = client.connect();
		return session;
	}

	public void passivateObject(Object arg0, Object arg1) throws Exception {
		
	}

	public boolean validateObject(Object arg0, Object arg1) {
		if (arg1 instanceof IoSession) {
			try {
				if (((IoSession) arg1).isConnected()) {
					 return true;
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return false;
	}

	public MinaPoolableObjectFactory() {
		super();
	}




}
