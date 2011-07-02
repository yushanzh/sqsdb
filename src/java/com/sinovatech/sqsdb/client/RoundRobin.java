package com.sinovatech.sqsdb.client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * RoundRobin类,均衡策略的粗粒度实现.
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-4-25
 */
public class RoundRobin implements Equalizer {
	
	private static final Log log = LogFactory.getLog(RoundRobin.class);
	
	
	public Object getMinaSession(MinaSessionManager manager, int code, String... socketAddress) {
		Object ioSession = null;
		List<String> list = new LinkedList(Arrays.asList(socketAddress));
		String addr = null;
		while(list.size()>0){
			int num = code%list.size();
			addr = list.remove(num);
			try {
				ioSession = manager.getMinaSession(addr);
				break;
			} catch (Exception e) {
				log.error(addr,e);
			}
		}
		
		return ioSession;
	}

}
