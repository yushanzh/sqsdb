/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sinovatech.sqsdb.common.pool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

/**
 * <p>An implementation of a Jakarta-Commons ObjectPool which
 * tracks objects and can recover abandoned objects.
 * If logAbandoned=true, a stack trace will be printed for any
 * abandoned objects recovered.
 *                                                                        
 * @author Glenn L. Nielsen
 * @version $Revision: 1.1 $ $Date: 2009/07/07 06:02:02 $
 */
public class AbandonedObjectPool extends GenericKeyedObjectPool {
	private static final Log log = LogFactory.getLog(AbandonedObjectPool.class);
	private static SimpleDateFormat format = new SimpleDateFormat("'FTPClient object is closed , created' yyyy-MM-dd HH:mm:ss ");
    /** 
     * DBCP AbandonedConfig 
     */
    private AbandonedConfig config = null;
    
    /**
     * A list of objects in use
     */
    private Map trace = new HashMap();

    /**
     * Create an ObjectPool which tracks objects.
     *
     * @param factory PoolableObjectFactory used to create this
     * @param config configuration for abandoned objects
     */
    public AbandonedObjectPool(KeyedPoolableObjectFactory factory,GenericKeyedObjectPool.Config cfg,
                               AbandonedConfig config) {
        super(factory,cfg);
        this.config = config;
        log.info("LogAbandoned: " + config.getLogAbandoned());
        log.info("RemoveAbandoned: " + config.getRemoveAbandoned());
        log.info("RemoveAbandonedTimeout: " + config.getRemoveAbandonedTimeout());
    }

    /**
     * Get a object from the pool.
     *
     * If removeAbandoned=true, recovers objects which
     * have been idle > removeAbandonedTimeout and
     * getNumActive() > getMaxActive() - 3 and
     * getNumIdle() < 2
     * 
     * @return Object
     * @throws Exception if an exception occurs retrieving a 
     * object from the pool
     */
    public Object borrowObject(Object key) throws Exception {
        if (config != null
                && config.getRemoveAbandoned()
                && (getNumIdle() < 2)
                && (getNumActive() > getMaxActive() - 3) ) {
            removeAbandoned();
        }
        Object obj = super.borrowObject(key);
        if (obj instanceof AbandonedTrace) {
            ((AbandonedTrace) obj).setLastUsed();
        }
        if (obj != null && config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
                trace.put(key, obj);
            }
        }
        return obj;
    }

    /**
     * Return a object to the pool.
     *
     * @param obj object to return
     * @throws Exception if an exception occurs returning the object
     * to the pool
     */
    public void returnObject(Object key, Object obj) throws Exception {
        if (config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
                Object foundObject = trace.remove(key);
                if (foundObject==null) {
                    return; // This object has already been invalidated.  Stop now.
                }
            }
        }
        super.returnObject(key,obj);
    }

    /**
     * Invalidates an object from the pool.
     *
     * @param obj object to be returned
     * @throws Exception if an exception occurs invalidating the object
     */
    public void invalidateObject(Object key, Object obj) throws Exception {
        if (config != null && config.getRemoveAbandoned()) {
            synchronized (trace) {
            	Object foundObject = trace.remove(key);
                if (foundObject==null) {
                    return; // This object has already been invalidated.  Stop now.
                }
            }
        }
        super.invalidateObject(key,obj);        
    }

    /**
     * Recover abandoned objects which have been idle
     * greater than the removeAbandonedTimeout.
     */
    private void removeAbandoned() {
        // Generate a list of abandoned objects to remove
        long now = System.currentTimeMillis();
        long timeout = now - (config.getRemoveAbandonedTimeout() * 1000);
        Map remove = new HashMap();
        synchronized (trace) {
            Iterator it = trace.entrySet().iterator();
            while (it.hasNext()) {
            	Entry v = (Entry)it.next();
                AbandonedTrace pc = (AbandonedTrace) v.getValue();
                if (pc.getLastUsed() > timeout) {
                    continue;
                }
                if (pc.getLastUsed() > 0) {
                    remove.put(v.getKey(), pc);
                }
            }
        }

        // Now remove the abandoned objects
        Iterator it = remove.entrySet().iterator();
        while (it.hasNext()) {
        	Entry v = (Entry)it.next();
            AbandonedTrace at = (AbandonedTrace) v.getValue();
            if (config.getLogAbandoned()) {
            	log.error(format.format(new Date(at.getLastUsed())));
                //pc.printStackTrace();
            }             
            try {
                invalidateObject(v.getKey(),at);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
}

