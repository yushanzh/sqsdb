package com.sinovatech.sqsdb.client;

import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.mina.core.session.IoSession;

import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.common.pool.MinaPoolableObjectFactory;
import com.sinovatech.sqsdb.socket.TCPClient;


public class MinaSessionManager {
	private static final Log log = LogFactory.getLog(MinaSessionManager.class);
	private KeyedPoolableObjectFactory factory =  null;
	private GenericKeyedObjectPool connectionPool = null;
	private static final long waitInterval = PropUtil.getInt("minasocket","client.waitInterval");
	
	/**
	 * 初始化实例池
	 * @param ioHandler
	 * @param config
	 * @param remoteAddress 池中对象连的服务器的IP及端口
	 */
	public MinaSessionManager() {
		try {
			GenericKeyedObjectPool.Config  cfg = new GenericKeyedObjectPool.Config();
			cfg.maxActive = PropUtil.getInt("minasocket","client.maxactive");
			cfg.maxIdle = PropUtil.getInt("minasocket","client.maxidle");
			cfg.minIdle = 0;
			cfg.maxWait = PropUtil.getInt("minasocket","client.maxwait");
			cfg.testOnBorrow = true;
			cfg.testWhileIdle = true;
			cfg.minEvictableIdleTimeMillis = PropUtil.getInt("minasocket","client.maxIdleTime");
			cfg.timeBetweenEvictionRunsMillis = PropUtil.getInt("minasocket","client.EvictionRunsMillis");
			cfg.whenExhaustedAction = GenericKeyedObjectPool.WHEN_EXHAUSTED_FAIL;//表示不等待,抛出一个java.util.NoSuchElementException异常
			cfg.numTestsPerEvictionRun = PropUtil.getInt("minasocket","client.maxactive");
			factory = new MinaPoolableObjectFactory();
			connectionPool = new GenericKeyedObjectPool(factory,cfg);
        } catch (Exception e) {
            log.error("初始化mina session失败", e);
        }
	}
	/**
	 * 获取实例.
	 * <p>
	 * 描述：
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @throws Exception 
	 * @since
	 */
	public IoSession getMinaSession(Object key) throws Exception   {
		IoSession client = null;
		try {
			client = (IoSession) connectionPool.borrowObject(key);
		} catch (NoSuchElementException e) {
			log.info("borrowObject exception sleep "+waitInterval+"ms active:"+getNumActive()+" & idle:"+getNumIdle());
			Thread.currentThread().sleep(waitInterval);
//			client = (IoSession) connectionPool.borrowObject(key);
			return getMinaSession(key);
		}
		log.debug("after get active:"+getNumActive()+" & idle:"+getNumIdle());
		return client;
	}
	
	/**
	 * 归还实例
	 * [简述].
	 * <p>
	 * 描述：
	 *
	 * @param key
	 * @param obj
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	public void returnMinaSession(Object key, Object obj) throws Exception {
		if (null != obj) {
			connectionPool.returnObject(key,obj);
		}
		log.debug("after return active & idle"+getNumActive()+" & "+getNumIdle());
	}
	
	/**
	 * 返回池中的自由对象数
	 * @return
	 */
	public int getNumIdle() {
		return connectionPool.getNumIdle();
	}
	
	/**
	 * 返回池中取出的对象数
	 * @return
	 */
	public int getNumActive() {
		return connectionPool.getNumActive();
	}
	
}
