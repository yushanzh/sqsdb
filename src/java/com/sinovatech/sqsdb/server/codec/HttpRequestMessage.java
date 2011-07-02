package com.sinovatech.sqsdb.server.codec;


public class HttpRequestMessage extends HttpMessage{
	
	public static final String HTTP_METHOD_GET = "GET";
	
	public static final String HTTP_METHOD_POST = "POST";
	
	private String NAME = "name=";
	private String CLASS = "&class=";
	private String ELEMENT = "&";
    
	private String requestMethod = HTTP_METHOD_GET;
	
	private String path = null;
	
	private String params = null;
	
	public HttpRequestMessage(){
	}
	
	/**
	 * 
	 * 实例化一个请求对象
	 * <p>
	 * 描述:
	 *
	 * @param path URL路径.如/poll
	 * @param params URL参数.如name=name&test=test
	 * @param method 采用POST或是GET方式
	 */
	public HttpRequestMessage(String path, String params, String method) {
		requestMethod = method;
		this.path = path;
		this.params = params;
    }
	
	/**
	 * 
	 * 实例化一个请求对象
	 * <p>
	 * 描述:
	 *
	 * @param path URL路径.如/poll
	 * @param params URL参数.如name=name&test=test
	 */
	public HttpRequestMessage(String path, String params) {
		this.path = path;
		this.params = params;
    }
	
	/**
	 * 
	 * 实例化一个请求对象
	 * <p>
	 * 描述:
	 *
	 * @param path URL路径.如/poll
	 * @param queueName 队列名称.
	 * @param classes 队列保存的Class,如果采用默认的Class,可为NULL
	 * @param dataParam URL参数.如test=test
	 * @param method  采用POST或是GET方式
	 */
	public HttpRequestMessage(String path,String queueName,Class classes,String dataParam, String method){
		requestMethod = method;
		this.path = path;
		StringBuffer sb = new StringBuffer();
		if(queueName != null){
			sb.append(NAME);
			sb.append(queueName);
		}
		if(classes != null){
			sb.append(CLASS);
			sb.append(classes.getName());
		}
		if(dataParam != null){
			sb.append(ELEMENT);
			sb.append(dataParam);
		}
		this.params = sb.toString();
	}
	
	/**
	 * 
	 * 实例化一个请求对象.
	 * <p>
	 * 描述:
	 *
	 * @param path URL路径.如/poll
	 * @param queueName 队列名称.
	 * @param classes 队列保存的Class,如果采用默认的Class,可为NULL
	 * @param dataParam URL参数.如test=test
	 */
	public HttpRequestMessage(String path,String queueName,Class classes,String dataParam){
		this.path = path;
		StringBuffer sb = new StringBuffer();
		if(queueName != null){
			sb.append(NAME);
			sb.append(queueName);
		}
		if(classes != null){
			sb.append(CLASS);
			sb.append(classes.getName());
		}
		if(dataParam != null){
			sb.append(ELEMENT);
			sb.append(dataParam);
		}
		this.params = sb.toString();
	}
	
	/**
	 * 返回HTTP采用的方式
	 * <p>
	 * 描述：
	 *
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * 设置HTTP采用的方式
	 * <p>
	 * 描述：
	 *
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * 返回URL路径
	 * <p>
	 * 描述：
	 *
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置URL路径
	 * <p>
	 * 描述：
	 *
	 * @param path
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getContext() {
        return path == null ? null : path.substring(1);
    }
	
	/**
	 * 返回URL参数
	 * <p>
	 * 描述：
	 *
	 * @return
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public String getParams() {
		return params;
	}
	
	/**
	 * 设置URL参数.
	 * <p>
	 * 描述：
	 *
	 * @param params
	 * @author wanghailong@sinovatech.com -- 2011-4-28
	 * @since
	 */
	public void setParams(String params) {
		this.params = params;
	}
	
	

}
