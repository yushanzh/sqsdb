package com.sinovatech.sqsdb.bdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * BDB数据库访问对象.
 * <p>
 * BDB数据库访问对象,包含各种索引.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-3-29
 */
public class DataAccessor {
	private static final Log log = LogFactory.getLog(DataAccessor.class);
    PrimaryIndex pi = null;
    String primaryKey = null;
    Map<String,EntityIndex> indexs = new ConcurrentHashMap();

    /**
     * 
     * 始初化BDB数据库访问对象.
     * <p>
     * 描述:
     * 始初化BDB数据库访问对象和索引.
     * @param store
     * @param obj
     */
    public DataAccessor(EntityStore store,Object obj) {

        // Primary key for QueueData classes
//    	pi = store.getPrimaryIndex(
//    			String.class, Object.class);
//    	si = store.getSecondaryIndex(pi, String.class, "");
    	Class classes = obj.getClass();

		Field[] fields = classes.getDeclaredFields();
		Annotation annotation = null;
		for (Field field : fields) {// 封装数据索引
			field.setAccessible(true);
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				annotation = field.getAnnotation(PrimaryKey.class);
				primaryKey = field.getName();
				pi = store.getPrimaryIndex(field.getType(), classes);
				indexs.put(field.getName(), pi);
			} else if (field.isAnnotationPresent(SecondaryKey.class)) {
				annotation = field.getAnnotation(SecondaryKey.class);
				SecondaryIndex si = store.getSecondaryIndex(pi,
						field.getType(), field.getName());
				indexs.put(field.getName(), si);
			}

		}
    	
    	
    }
}
