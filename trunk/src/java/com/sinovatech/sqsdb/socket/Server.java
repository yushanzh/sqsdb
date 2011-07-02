package com.sinovatech.sqsdb.socket;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;




public abstract class Server {
	
	static Logger logger = Logger.getLogger(Server.class.getName());
	private int port = 4091;
	private IoHandler ioHandler = null;
	private String ip = null;
	/**
	 * 接收器
	 */
	private IoAcceptor acceptor = null;
    private String charset="UTF-8";
//    private IoAcceptorConfig ioAcceptorConfig;
	
	/**
     * 构造方法
     */
    public Server(String ioHandlerName)
    {
        this.setParam(HandlerFactory.getIoHandler(ioHandlerName));
      
    }
    
    /**
     * 构造方法，参数port，ioHandler(io处理类)
     */
    public Server(IoHandler ioHandler) {
		this.setParam(ioHandler);
				
    }

	/**
	 * 基本参数设置
	 * @param ip
	 * @param port
	 * @param ioHandler io处理类
	 */
	public void setParam(IoHandler ioHandler) {
//        this.port = port;
        this.ioHandler = ioHandler;
//        ByteBuffer.setUseDirectBuffers(false);
//        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
	}

	public void start() throws IOException {
//		acceptor.setHandler(ioHandler);
		if(ip==null)
			acceptor.bind(new InetSocketAddress(port));
		else
			acceptor.bind(new InetSocketAddress(ip,port));
	}
	
	/**
	 * 关闭监听
	 * @throws Exception
	 */	
	public void shutdown() {
//		if(ip==null)
//			acceptor.unbind(new InetSocketAddress(port));
//		else
//			acceptor.unbind(new InetSocketAddress(ip,port));
		acceptor.dispose();
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
	 * 增加ioFilter过滤
	 * @return
	 */		
	public void addLast(String name, IoFilter ioFilter) {
		acceptor.getFilterChain().addLast( name, ioFilter );
	}
	/**
	 * 增加ioFilter过滤
	 * @return
	 */		
	public void addFirst(String name, IoFilter ioFilter) {
		acceptor.getFilterChain().addFirst( name, ioFilter );
	}
	
	public void addLogLast(){
		this.addLast("logging", new LoggingFilter());
	}
	

	public IoAcceptor getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(IoAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public IoHandler getIoHandler() {
		return ioHandler;
	}

	public void setIoHandler(IoHandler ioHandler) {
		this.ioHandler = ioHandler;
		acceptor.setHandler(ioHandler);
	}

//	public IoAcceptorConfig getIoAcceptorConfig() {
//		return ioAcceptorConfig;
//	}
//
//	public void setIoAcceptorConfig(IoAcceptorConfig socketAcceptorConfig) {
//		this.ioAcceptorConfig = socketAcceptorConfig;
//	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
