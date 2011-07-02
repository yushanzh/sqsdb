package com.sinovatech.sqsdb.client;

/**
 * 均衡策略接口.
 * <p>
 * 均衡策略接口.
 * </p>
 * Copyright(c) 2011 http://www.sinovatech.com/
 * @author wanghailong@sinovatech.com
 * @version 1.0, 2011-4-25
 */
public interface Equalizer {
	/**
	 * 按均衡策略获取实例.
	 * <p>
	 * 描述：
	 *
	 * @param manager
	 * @param code
	 * @param socketAddress
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-25
	 * @since
	 */
	Object getMinaSession(MinaSessionManager manager, int code, String... socketAddress);
}
