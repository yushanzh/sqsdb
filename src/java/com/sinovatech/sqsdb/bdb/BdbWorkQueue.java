package com.sinovatech.sqsdb.bdb;


import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


import com.sinovatech.sqsdb.common.ObjectFactory;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sleepycat.collections.CurrentTransaction;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityStore;

/**
 * 
 * BdbWorkQueue队列.
 * <p>
 * BDB队列的实现.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-3-28
 */
public class BdbWorkQueue<E> extends WorkQueue<E> {

	private static final Log log = LogFactory.getLog(BdbWorkQueue.class);
	
	private static final String KEY_FLAG = PropUtil.getString("sqsconfig", "bdb.key.flag");
	private static final int NEXT_MAX = PropUtil.getInt("sqsconfig", "bdb.maxnext");

	private DataAccessor accessor;

	// Encapsulates the environment and data store.
	private static BdbEnvironment myDbEnv = new BdbEnvironment();

	private EntityCursor<E> cursor = null;
	private int nextnum = 0;
	
	private static Vector<EntityCursor> cursors = new Vector<EntityCursor>();
	private static Vector<Transaction> txns = new Vector<Transaction>();
	
	//	private Transaction txn = null;
	//    private static Cursor cursor=null;
	
	private String name = null;
	
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				new BdbWorkQueue().stop();
			}
		});
	}
	
	BdbWorkQueue(){}
	
	public BdbWorkQueue(String name) {
		this.name = name;
	}
	
	public void init() throws ClassNotFoundException{
		EntityStore store = myDbEnv.createStore(name);
		String className =  myDbEnv.getStoreClass(store);
		if(className==null){
			throw new ClassNotFoundException("queue object is null");
		}
		Object obj = ObjectFactory.getInstance(className);
		accessor = new DataAccessor(store, obj);
		Transaction txn = myDbEnv.getEnv().beginTransaction(null, null);
		cursor = accessor.pi.entities(txn,CursorConfig.READ_UNCOMMITTED);
		txns.add(txn);
		cursors.add(cursor);
	}
	
	public void setup(Object obj){
		
		// Open the data accessor. This is used to store
		// persistent objects.
		accessor = new DataAccessor(myDbEnv.createStore(name), obj);
//		accessor.pi.getDatabase().getConfig().setSortedDuplicates(true);
//		accessor.pi.getDatabase().getConfig().setTransactional(true);
//		CursorConfig config = new CursorConfig();
		Transaction txn = myDbEnv.getEnv().beginTransaction(null, null);
		
		cursor = accessor.pi.entities(txn, CursorConfig.READ_UNCOMMITTED);
		txns.add(txn);
		cursors.add(cursor);
		//		cursor = accessor.pi.getDatabase().openCursor(null, null);
	}

//	public BdbWorkQueue(TestConfig config) {
//		myDbEnv.setup(config);
//		accessor = new DataAccessor(myDbEnv.createStore(config.getStoreName()),
//				new Object());
//		cursor = accessor.pi.entities();
//		//    	cursor = accessor.pi.getDatabase().openCursor(null, null);
//	}

	@Override
	public Object add(E value) {
		Object obj = accessor.pi.put(value);
		log.debug("add() already exists Object: "+obj);
		return obj;
	}

	@Override
	public synchronized E poll() throws Exception {//A transaction was not supplied when opening this cursor: delete
//		long begin = System.currentTimeMillis();
		E e = null;
		nextnum++;
		if(nextnum > NEXT_MAX){
			nextnum = 0;
			e = cursor.first();
		}else{
			e = cursor.next();
			if(e == null){
				nextnum = 0;
				e = cursor.first();
			}
		}
		
//		log.info("next time === "+(System.currentTimeMillis()-begin));
//		begin = System.currentTimeMillis();
		if(e!=null){
			Object key = PropertyUtils.getProperty(e, accessor.primaryKey);
			accessor.pi.delete(null, key);
		}
//		log.info("delete time === "+(System.currentTimeMillis()-begin));
		return e;
	}

	@Override
	public Object getByField(String fieldName, Object key) {
		return accessor.indexs.get(fieldName).get(key);
		
	}
	
	@Override
	public long getCountByField(String fieldName){
		if(fieldName==null || "".equals(fieldName)){
			return accessor.pi.count();
		}else{
			return accessor.indexs.get(fieldName).count();
		}
	}

	@Override
	public long getPositionByField(String fieldName, Object key) {
		EntityIndex index = accessor.indexs.get(fieldName);
		if(index.get(key)==null)
			return -1L;
		EntityCursor<E> cursor = index.entities(CurrentTransaction.getInstance(myDbEnv.getEnv()).getTransaction(),"0", false, key, false,CursorConfig.READ_UNCOMMITTED);
		long l = 0L;
		try{
			while(cursor.next()!=null){
				l++;			
			}
		}finally{
			cursor.close();
		}
		return l;
	}

	@Override
	public boolean removeByField(String fieldName, Object key) {
		return accessor.indexs.get(fieldName).delete(key);
	}

	@Override
	public boolean setHeadByField(String fieldName, Object key) throws Exception {
		EntityIndex index =  accessor.indexs.get(fieldName);
		Transaction txn = myDbEnv.getEnv().beginTransaction(CurrentTransaction.getInstance(myDbEnv.getEnv()).getTransaction(), null);
		
		EntityCursor<E> cursor = index.entities(txn, key, true, key, true, CursorConfig.DEFAULT);
		try{
			E v = cursor.next();
	
			E v2 = this.cursor.first();
			long min = 0L;
			
			if(v != null && v2 != null){
				min = Long.parseLong(BeanUtils.getProperty(v2, accessor.primaryKey));
			}
	
			while(v!=null && min != 0L){
				min--;
				BeanUtils.setProperty(v, accessor.primaryKey, min);
	//			cursor.update(v);
				cursor.delete();
				accessor.pi.put(txn,v);
				v = cursor.next();
			}
		}finally{
	
			cursor.close();
			txn.commit();
		}
		return true;
	}

	@Override
	public boolean setPositionByField(String fieldName, Object key,
			long addValue) throws Exception {
		EntityIndex index = accessor.indexs.get(fieldName);
		Transaction txn = myDbEnv.getEnv().beginTransaction(CurrentTransaction.getInstance(myDbEnv.getEnv()).getTransaction(), null);
		EntityCursor<E> cursor = index.entities(txn, key, true, key, true, CursorConfig.DEFAULT);

		try{
			E v = cursor.next();
			long curr = 0L;
	
			while (v != null) {
	
				curr = Long.parseLong(BeanUtils.getProperty(v, accessor.primaryKey));
				BeanUtils.setProperty(v, accessor.primaryKey, (curr+addValue)+KEY_FLAG);//todo:最后一位加上标识位,此处实现不完整,应该第次删除之后再加上
	
	//			cursor.update(v);
				cursor.delete();
				accessor.pi.put(txn,v);
				v = cursor.next();
			}
		}finally{
			cursor.close();
			txn.commit();
		}
		return true;
	}
	
	public void stop(){
		for(int i=0; i<cursors.size(); i++){
			cursors.get(i).close();
			txns.get(i).commit();
		}
		cursors.clear();
		txns.clear();
		myDbEnv.close();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
