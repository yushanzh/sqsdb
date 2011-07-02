package com.sinovatech.sqsdb.bdb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * 
 * WorkQueue接口.
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-3-28
 */
public abstract class WorkQueue<E> {

	public void setup(E value){
		
	}
	public void init() throws ClassNotFoundException{
		
	}
	/**
	 * 在此映射中关联指定值与指定键。
	 * [简述].
	 * <p>
	 * 描述：
	 *　在此映射中关联指定值与指定键。如果此映射以前包含了一个该键的映射关系，则旧值被替换。
	 * @param key 指定值将要关联的键。
	 * @param value 指定键将要关联的值。
	 * @return 与指定键相关联的旧值，如果键没有任何映射关系，则返回 null。返回 null 还可能表示该 HashMap 以前将 null 与指定键关联。
	 * @author wanghailong@sinovatech.com -- 2011-3-29
	 * @since
	 */
	public abstract Object add(E value);
	/**
	 * 检索并移除此队列的头，如果此队列为空，则返回 null。
	 * [简述].
	 * <p>
	 * 描述：
	 * 检索并移除此队列的头，如果此队列为空，则返回 null。
	 * @return 
	 * @author wanghailong@sinovatech.com -- 2011-3-30
	 * @throws Exception 
	 * @since
	 */
	public abstract E poll() throws Exception;
	
	/**
	 * 如果此映射中存在该键的映射关系，则将其删除。
	 * [简述].
	 * <p>
	 * 描述：
	 * 如果此映射中存在该键的映射关系，则将其删除。
	 * @param key 其映射关系要从映射中移除的键。
	 * @return 与指定键相关联的旧值，如果键没有任何映射关系，则返回 null。返回 null 还可能表示该映射以前将 null 与指定键关联。
	 * @author wanghailong@sinovatech.com -- 2011-3-29
	 * @since
	 */
	public abstract boolean removeByField(String fieldName, Object key);
	
	/**
	 * 按条件把数据提到队列头部.
	 * [简述].
	 * <p>
	 * 描述：
	 * 把字段上所有指定值的数据提到队列头部
	 * @param fieldName
	 * @param key
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract boolean setHeadByField(String fieldName, Object key) throws Exception;
	
	/**
	 * 返回队列长度.
	 * [简述].
	 * <p>
	 * 描述：
	 * 返回指定字段的长度
	 * @param fieldName
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract long getCountByField(String fieldName);
	
	/**
	 * 设置数据对象的位置.参数为正或负,可以提前和推后在队列的位置.
	 * [简述].
	 * <p>
	 * 描述：
	 * 把字段上所有指定值的数据移动位置,需要客户端的KEY是唯一的,且可转为数字类型
	 * @param fieldName
	 * @param key
	 * @param addValue
	 * @return
	 * @throws Exception
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract boolean setPositionByField(String fieldName, Object key, long addValue) throws Exception;
	/**
	 * 计算字段对应的值的位置
	 * [简述].
	 * <p>
	 * 描述：
	 * 
	 * @param fieldName
	 * @param key
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract long getPositionByField(String fieldName, Object key);
	/**
	 * 按条件返回一个数据对象
	 * [简述].
	 * <p>
	 * 描述：
	 *
	 * @param fieldName
	 * @param key
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract Object getByField(String fieldName, Object key);
	/**
	 * 停止队列服务
	 * [简述].
	 * <p>
	 * 描述：
	 * 停止队列服务
	 * @author wanghailong@sinovatech.com -- 2011-4-23
	 * @since
	 */
	public abstract void stop();
}
