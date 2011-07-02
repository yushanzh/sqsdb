package com.sinovatech.sqsdb.socket;

import java.io.Serializable;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;


public abstract class Client {

	static Logger logger = Logger.getLogger(Client.class.getName());

	private String ip = "127.0.0.1";
	private int port = 80;
    private IoHandler ioHandler = null;
    private ConnectFuture connectFuture;
    private String charset="UTF-8";
    private IoConnector connector;
//    private IoServiceConfig ioServiceConfig;

	/**
     * 构造方法
     */
    public Client(String ip, int port,String ioHandlerName)
    {
        this.setParam(ip, port, HandlerFactory.getIoHandler(ioHandlerName));
    }
    /**
     * 无参构造方法.
     * <p>
     * 描述:
     *
     */
    public Client(){
    	
    }
    
    /**
     * 构造方法，参数ip, port，ioHandler(io处理类)
     */
    public Client(String ip, int port,IoHandler ioHandler) {
		this.setParam(ip, port, ioHandler);
    }

	/**
	 * 基本参数设置
	 * @param ip
	 * @param port
	 * @param ioHandler io处理类
	 */
	public void setParam(String ip, int port,IoHandler ioHandler) {
        this.ip = ip;
        this.port = port;
        this.ioHandler = ioHandler;
//        ByteBuffer.setUseDirectBuffers(false);
//        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
	}
	
	/**
	 * 连接指定的ip，port, 并指定IO处理类
	 * @param ip
	 * @param port
	 * @param ioHandler
	 */	
	public IoSession connect(){
		connectFuture = connector.connect(new InetSocketAddress(ip,port));
		connectFuture.awaitUninterruptibly();
		return connectFuture.getSession();
	}
	

	
	/**
	 * 写数据
	 * @param data
	 * @throws Exception
	 */
	public void write(Object data) throws Exception{
		WriteFuture future = connectFuture.getSession().write(data);
//		future.awaitUninterruptibly();
	}
	
	/**
	 * 写数据
	 * @param data
	 * @throws Exception
	 */
	public void write(byte[] data) throws Exception{
		WriteFuture future = connectFuture.getSession().write(data);
//		future.awaitUninterruptibly();
	}
	
	/**
	 * 写数据
	 * @param data
	 * @throws Exception
	 */
	public void write(String data) throws Exception{
		WriteFuture future = connectFuture.getSession().write(data);
//		future.awaitUninterruptibly();
	}
	
	/**
	 * 写数据
	 * @param data
	 * @throws Exception
	 */
//	public void write(Serializable data) throws Exception{
//		WriteFuture future = connectFuture.getSession().write(data);
//		future.join();
//		future.addListener(IoFutureListener.CLOSE);//这个的作用是发送完毕后关闭连接，加了就是短连接，不然是长连接
//	}
	
	/** 
	 * 关闭session
	 */
	public void close() throws Exception{
		if ( connectFuture.isConnected() ) {
			connectFuture.getSession().close(true);
		}				
	}
	
	
	/**
	 * 增加ioFilter过滤
	 * @return
	 */		
	public void addLast(String name, IoFilter ioFilter) {
		connector.getFilterChain().addLast( name, ioFilter );
	}
	/**
	 * 增加ioFilter过滤
	 * @return
	 */		
	public void addFirst(String name, IoFilter ioFilter) {
		connector.getFilterChain().addFirst( name, ioFilter );
	}
	
	public void addLogLast(){
		this.addLast("logging", new LoggingFilter());
	}

	/**
	 * 获取端口值
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置端口
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 获取IP地址
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP地址
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取当前Socket的IOHandler
	 * @return
	 */
	public IoHandler getIoHandler() {
		return ioHandler;
	}

	/**
	 * 设置当前Socket的IOHandler
	 * @return
	 */
	public void setIoHandler(IoHandler ioHandler) {
		this.ioHandler = ioHandler;
		connector.setHandler(ioHandler);
	}

	/**
	 * 获取当前Socket的Text模式下的字符集
	 * @return
	 */	
	public String getCharset() {
		return charset;
	}

	/**
	 * 设置当前Socket的Text模式下的字符集
	 * @return
	 */		
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 获取当前连接
	 * @return
	 */
	public IoConnector getConnector() {
		return connector;
	}

	/**
	 * 设置连接
	 * @param connector
	 */
	public void setConnector(IoConnector connector) {
//		connector.setHandler(ioHandler);
		this.connector = connector;
	}

	/**
	 * 获取当前ConnectFuture
	 * @return
	 */
	public ConnectFuture getConnectFuture() {
		return connectFuture;
	}

	/**
	 * 获取当前ConnectFuture
	 * @return
	 */
	public void setConnectFuture(ConnectFuture cf) {
		this.connectFuture = cf;
	}

	/**
	 * @return the ioServiceConfig
	 */
//	public IoServiceConfig getIoServiceConfig() {
//		return ioServiceConfig;
//	}

	/**
	 * @param ioServiceConfig the ioServiceConfig to set
	 */
//	public void setIoServiceConfig(IoServiceConfig ioServiceConfig) {
//		this.ioServiceConfig = ioServiceConfig;
//	}
	
	
}
