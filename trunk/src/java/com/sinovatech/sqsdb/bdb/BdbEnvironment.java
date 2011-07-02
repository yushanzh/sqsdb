package com.sinovatech.sqsdb.bdb;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.sqsdb.bdb.MyExample.TestConfig;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Durability;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
/**
 * 
 * BdbEnvironment类简述:BDB环境初始化和数据库管理.
 * <p>
 * BDB环境初始化和数据库管理.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-4-23
 */
public class BdbEnvironment {
	private static final Log log = LogFactory.getLog(BdbEnvironment.class);

    private static Environment myEnv = null;

//    private EntityStore store;
    private static Map stores = new ConcurrentHashMap();

    // Our constructor does nothing
    public BdbEnvironment() {}

    // The setup() method opens the environment and store
    // for us.
    static {
		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		// StoreConfig storeConfig = new StoreConfig();
		// DatabaseConfig dataConfig = new DatabaseConfig();

		myEnvConfig.setReadOnly(false);
		// storeConfig.setReadOnly(readOnly);

		// If the environment is opened for write, then we want to be
		// able to create the environment and entity store if
		// they do not exist.
		myEnvConfig.setAllowCreate(true);
		// storeConfig.setAllowCreate(!readOnly);
		myEnvConfig.setTransactional(true);
		myEnvConfig.setTxnTimeout(5000000, TimeUnit.MICROSECONDS);
		// storeConfig.setTransactional(false);
		// storeConfig.setTemporary(true);
		// storeConfig.setDeferredWrite(true);
		if (PropUtil.getString("sqsconfig", "bdb.CacheSize")!=null && !"".equals(PropUtil.getString("sqsconfig", "bdb.CacheSize"))) {
			myEnvConfig.setCacheSize(PropUtil.getLong("sqsconfig",
					"bdb.CacheSize"));
		}
		myEnvConfig.setSharedCache(true);
		if (PropUtil.getString("sqsconfig", "bdb.log.fileMax")!=null && !"".equals(PropUtil.getString("sqsconfig", "bdb.log.fileMax"))) {
			myEnvConfig.setConfigParam("je.log.fileMax", PropUtil.getString("sqsconfig","bdb.log.fileMax"));
		}
		//Can't set 'je.env.isNoLocking' and 'je.env.isTransactional'
		myEnvConfig.setLocking(true);
		myEnvConfig.setLockTimeout(5000000, TimeUnit.MICROSECONDS); 
		
		int dur = PropUtil.getInt("sqsconfig", "bdb.Durability");
		if(dur == 1)
			myEnvConfig.setDurability(Durability.COMMIT_SYNC);
		else if(dur == 2)
			myEnvConfig.setDurability(Durability.COMMIT_WRITE_NO_SYNC);
		else
			myEnvConfig.setDurability(Durability.COMMIT_NO_SYNC);
		
		myEnvConfig.setTxnSerializableIsolation(false);
		// Open the environment and entity store
		myEnv = new Environment(new File(PropUtil.getString("sqsconfig", "bdb.path")), myEnvConfig);
		// store = new EntityStore(myEnv, "EntityStore1", storeConfig);

		// TransactionConfig config = new TransactionConfig();
		// config.setReadUncommitted(true);

		// txn.commit();
//		myEnv.setThreadTransaction(txn);
		
	}

    /**
     * 创建或打开一个新的数据库
     * [简述].
     * <p>
     * 描述：
     * 创建或打开一个新的数据库,如数据库存在则打开数据库,如不存在,则按名称创建新的数据库
     * @param storeName 数据库名
     * @return
     * @author wanghailong@sinovatech.com -- 2011-4-23
     * @since
     */
    public EntityStore createStore(String storeName) {

		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
// 		storeConfig.setExclusiveCreate(true);
		//If the store is transactional, the cursor may not be used to update or delete entities.
		//exclusive properties are true: deferredWrite, temporary, transactional
		//Non-transactional Cursors may not be used in multiple threads;
		storeConfig.setTransactional(true);
//		storeConfig.setTemporary(false);
//		storeConfig.setDeferredWrite(true);
		
		EntityStore store = new EntityStore(myEnv, storeName, storeConfig);
		stores.put(storeName, store);
		return store;
	}
    
    /**
     * 获取数据库存储的数据对象
     * <p>
     * 描述：
     * 获取数据库存储的数据对象,如果没有存储的数据对象则返回空
     * @param store 数据库名
     * @return
     * @author wanghailong@sinovatech.com -- 2011-4-23
     * @since
     */
    public String getStoreClass(EntityStore store){
    	Set set = store.getModel().getKnownClasses();//是否可以
    	Iterator it = set.iterator();
    	String className = null;
    	String temp = null;
    	while(it.hasNext()){
    		temp = (String)it.next();
    		if(temp.indexOf("com.sleepycat.persist")<0){
    			className = temp;
    			break;
    		}
    			
    	}
    	log.debug("className : "+className);
    	
    	return className;
    }
    
    public void setup(TestConfig testConfig) {
    	/* Create a new, transactional database environment. */
    	EnvironmentConfig envConfig = new EnvironmentConfig();
    	envConfig.setAllowCreate(true);
    	envConfig.setTransactional(testConfig.useTxns);
    	envConfig.setCacheSize(testConfig.cacheSize);
    	envConfig.setLocking(false);
    	envConfig.setDurability(Durability.COMMIT_NO_SYNC);
    	myEnv = new Environment(new File(testConfig.envHome), envConfig);

    	/* Open the entity store. */
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setAllowCreate(true);
        storeConfig.setTransactional(testConfig.useTxns);
        storeConfig.setDeferredWrite(testConfig.deferredWrite);
//        store = new EntityStore(myEnv, "EntityStore2", storeConfig);

    }

    // Return a handle to the entity store
    public Map getStores() {
        return stores;
    }

    // Return a handle to the environment
    public Environment getEnv() {
        return myEnv;
    }

    /**
     * 关闭数据库
     * <p>
     * 描述：
     * 进行持久化之后,关闭数据库
     * @author wanghailong@sinovatech.com -- 2011-4-23
     * @since
     */
    public void close() {
        if (!stores.isEmpty()) {
        	log.info("stores close() start ...");
        	Iterator it = stores.values().iterator();
        	EntityStore store = null;
        	while(it.hasNext()){
        		try {
                	store = (EntityStore)it.next();
            		store.sync();
                    store.close();
                } catch(DatabaseException dbe) {
                    log.error("Error closing store: "+ store.getStoreName(), dbe);
                }
        		
        	}
        }

        if (myEnv != null) {
            try {
                // Finally, close the store and environment.
            	myEnv.sync();
            	myEnv.cleanLog();
                myEnv.close();
            } catch(DatabaseException dbe) {
            	log.error("Error closing MyDbEnv: " , dbe);
            }
        }
        
        log.info("stores close() end ...");
    }


}
