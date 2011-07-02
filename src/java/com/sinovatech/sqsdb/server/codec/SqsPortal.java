package com.sinovatech.sqsdb.server.codec;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.sqsdb.bdb.AccessorProvider;
import com.sinovatech.sqsdb.bdb.WorkQueue;
import com.sinovatech.sqsdb.common.ObjectFactory;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.server.common.IHandler;

/**
 * 
 * HTTP请求处理实现.
 * 
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-3-24
 */
public class SqsPortal implements IHandler{
	private static final Log log = LogFactory.getLog(SqsPortal.class);
	private static final String NAME = "name";
	private static final String CLASS = "class";
	private static final String DEFAULTCLASS = PropUtil.getString("sqsconfig", "defaultClass");
	private static final String SUCCEED = "succeed";
	private static final String KEY = "key";
	private static final String VAR = "var";
	private static final String KV = "=";
	private static final String ELEMENT = "&";
	
	/**
	 * 添加数据对象到队列.
	 * <p>
	 *  例:http://127.0.0.1:8080/put?name=test&content=english00&pk=9999
	 * </p>
	 * @param request
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-3-24
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public String put(HttpRequestMessage request) throws IllegalAccessException, InvocationTargetException, ClassNotFoundException{
		
		String dbName = request.headers.remove(NAME);//队列名
		
		String className = request.headers.remove(CLASS);
		if(className==null){
			className = DEFAULTCLASS;
		}
		Object obj = ObjectFactory.getInstance(className);
		
		for (Iterator it = request.headers.entrySet().iterator(); it.hasNext();) {
			Entry<String,Object> entry = (Entry<String,Object>) it.next();
			BeanUtils.setProperty(obj, entry.getKey(), entry.getValue());
		}
		
		//保存对象
		WorkQueue queue = AccessorProvider.getQueue(dbName, obj);
		queue.add(obj);

		return SUCCEED;
	}
	
	
	/**
	 * 检索并移除此队列的头，如果此队列为空,则返回null.
	 * <p>
	 *  例:http://127.0.0.1:8080/poll?name=test
	 * </p> 
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	public String poll(HttpRequestMessage request) throws Exception {
//		long begin = System.currentTimeMillis();
		String dbName = request.getParameter(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		Object obj = null;
		StringBuffer sb = new StringBuffer();
		if(queue !=null ){
			obj = queue.poll();
			if(obj == null)
				return "null";
			Map map = BeanUtils.describe(obj);
			map.remove(CLASS);
			Set entries = map.entrySet();
			Iterator<Map.Entry<String,String>> it = entries.iterator();
			
			while(it.hasNext()){
				Map.Entry<String, String> entry = it.next();
				if(entry.getValue()!=null){
					sb.append(entry.getKey());
					sb.append(KV);
					sb.append(entry.getValue());
					sb.append(ELEMENT);
				}
		   } 
			
		}
//		log.debug("pollmethod time === "+(System.currentTimeMillis()-begin));
		return sb.toString();
	}
	
	/**
	 * 如果此队列中存在该数据对象，则将其删除。
	 * <p>
	 *  例:http://127.0.0.1:8080/remove?name=test&pk=9999
	 * </p>
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public String remove(HttpRequestMessage request) throws ClassNotFoundException{
		String dbName =request.headers.remove(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		Entry<String,String> entry = request.headers.entrySet().iterator().next();
		
		if(queue != null){
			return queue.removeByField(entry.getKey(), entry.getValue())+"";
		}
		
		return "-1";
	}
	
	/**
	 * 把指定的数据对象提到队头.
	 * <p>
	 *  例:http://127.0.0.1:8080/setHead?name=test&pk=9999
	 * </p> 
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	public String setHead(HttpRequestMessage request) throws Exception{
		String dbName = request.headers.remove(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		Entry<String,String> entry = request.headers.entrySet().iterator().next();
		
		if(queue != null){
			return queue.setHeadByField(entry.getKey(), entry.getValue())+"";
		}
		
		return "-1";
	}
	
	/**
	 * 设置数据对象的位置.参数为正或负,可以提前和推后在队列的位置.
	 * <p>
	 *  例:http://127.0.0.1:8080/setPosition?name=test&pk=9999&var=1000
	 * </p>
	 * 把娄据对象的位置移动指定位置,需要客户端的KEY是唯一的,且可转为数字类型
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	public String setPosition(HttpRequestMessage request) throws Exception{
		String dbName = request.headers.remove(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		long var = Long.parseLong(request.headers.remove(VAR));
		Entry<String,String> entry = request.headers.entrySet().iterator().next();
		
		
		if(queue != null){
			return queue.setPositionByField(entry.getKey(), entry.getValue(), var)+"";
		}
		
		return "-1";
	}
	
	/**
	 * 返回数据对象所在队列的长度
	 * <p>
	 *  例:http://127.0.0.1:8080/getCount?name=test&key=pk
	 * </p>
	 * @param request
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public String getCount(HttpRequestMessage request) throws ClassNotFoundException{
		String dbName = request.getParameter(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		String key = request.getParameter(KEY);
		
		if(queue != null){
			return queue.getCountByField(key)+"";
		}
		
		return "-1";
	}
	
	/**
	 * 返回数据对象的位置
	 * <p>
	 *  例:http://127.0.0.1:8080/getPosition?name=test&pk=9999
	 * </p>
	 * @param request
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public String getPosition(HttpRequestMessage request) throws ClassNotFoundException{
		String dbName = request.headers.remove(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
//		log.debug("queue is " + queue);
		Entry<String,String> entry = request.headers.entrySet().iterator().next();
		
		if(queue != null){
			return queue.getPositionByField(entry.getKey(), entry.getValue())+"";
		}
		
		return "-1";
	}
	
	/**
	 * 检索并返回数据对象，如果此队列为空,则返回空字符串
	 * <p>
	 *  例:http://127.0.0.1:8080/get?name=test&pk=9999
	 * </p>
	 * @param request
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @throws ClassNotFoundException 
	 * @since
	 */
	public String get(HttpRequestMessage request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
		String dbName = request.headers.remove(NAME);//队列名
		WorkQueue queue = AccessorProvider.getQueue(dbName);
		Object obj = null;
		StringBuffer sb = new StringBuffer();
//		log.debug("queue is " + queue);
		Entry<String,String> entry = request.headers.entrySet().iterator().next();
		
		if(queue != null){
			obj = queue.getByField(entry.getKey(), entry.getValue());
			if(obj == null)
				return "null";
			Map map = BeanUtils.describe(obj);
			map.remove(CLASS);
			Set entries = map.entrySet();
			Iterator<Map.Entry<String,String>> it = entries.iterator();
			
			while(it.hasNext()){
				Map.Entry<String, String> temp = it.next();
				if(temp.getValue()!=null){
					sb.append(temp.getKey());
					sb.append(KV);
					sb.append(temp.getValue());
					sb.append(ELEMENT);
				}
		   }
		}
		
		return sb.toString();
		
	}
	
	/**
	 * 停止队列服务
	 * <p>
	 *  例:http://127.0.0.1:8080/stop
	 * </p> 
	 * @param request
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	public void stop(HttpRequestMessage request){
		HttpServer.shutdown();
//		System.exit(0);
	}
}
