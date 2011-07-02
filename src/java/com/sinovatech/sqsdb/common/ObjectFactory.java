package com.sinovatech.sqsdb.common;

import org.apache.log4j.Logger;



public class ObjectFactory {
	static Logger logger = Logger.getLogger(ObjectFactory.class.getName());
	
	public static Object getInstance(String className,Object...objects) {
		
		Object object = null;
		try {
			logger.debug("ObjectFactory newInstance:"+className);
			if(objects==null || objects.length==0){
				object = Class.forName(className).newInstance();
			}else{
				Class[] c = new Class[objects.length];
				for(int i=0;i<c.length;i++){
					c[i] = objects[i].getClass();
				}
				object = Class.forName(className).getConstructor(c).newInstance(objects);
			}
		} catch (Exception e) {
			logger.error(className,e);
		}
		return object;
	}
}
