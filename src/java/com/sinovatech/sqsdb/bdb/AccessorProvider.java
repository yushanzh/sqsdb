package com.sinovatech.sqsdb.bdb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.sqsdb.common.ObjectFactory;
import com.sinovatech.sqsdb.common.PropUtil;

/**
 * 
 * AccessorProvider类简述: 队列访问提供者.
 * <p>
 * 队列访问提供者.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-3-28
 */
public class AccessorProvider {
	private static final Log log = LogFactory.getLog(AccessorProvider.class);
	
	private static Map maps = new HashMap();
	
	private static String QUEUE_CLASS = PropUtil.getString("sqsconfig", "queue.class");
	
	/**
	 * 根据队列名字获取队列对象,如果队列不存在根据可变参数进行创建队列.
	 * <p>
	 * 描述：
	 * 用于获取队列对象,在队列提供对象之后自动进行缓存
	 * @param name 队列名
	 * @param obj 队列数据对象
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public static WorkQueue getQueue(String name, Object...obj) throws ClassNotFoundException{
		WorkQueue sqs = null;
		if(name==null){
			sqs = (WorkQueue)ObjectFactory.getInstance((QUEUE_CLASS));
		}else{
			if(maps.get(name)==null){
				synchronized(maps){
					if(maps.get(name)==null){
						if(obj.length>0){
							sqs = (WorkQueue)ObjectFactory.getInstance((QUEUE_CLASS),name);
							sqs.setup(obj[0]);
							maps.put(name, sqs);
						}else{
							sqs = (WorkQueue)ObjectFactory.getInstance((QUEUE_CLASS),name);
							sqs.init();
							maps.put(name, sqs);
						}
					}else{
						sqs = (WorkQueue)maps.get(name);
					}
				}
			}else{
				sqs = (WorkQueue)maps.get(name);
			}
		}
		
		return sqs;
		
	}
}
