package com.sinovatech.sqsdb.socket;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.sinovatech.sqsdb.common.PropUtil;


public class TCPServer extends Server {
	
	/**
	 * TCP服务端对象
	 * @param ioHandler 处理Handler,实现于IoHandler
	 */
    public TCPServer(IoHandler ioHandler) {
    	super(ioHandler);
		this.setPort(PropUtil.getInt("minasocket","server.prot"));
    	
		if(PropUtil.getString("minasocket","server.ip")!=null && !"".equals(PropUtil.getString("minasocket","server.ip")))
    		this.setIp(PropUtil.getString("minasocket","server.ip"));

		NioSocketAcceptor acceptor = null;
    	if(PropUtil.getString("minasocket","socket.ThreadSize")!=null && !"".equals(PropUtil.getString("minasocket","socket.ThreadSize"))){//线程池数程数量
    		int threadSize = Integer.parseInt(PropUtil.getString("minasocket","socket.ThreadSize"));
    		acceptor = new NioSocketAcceptor(threadSize); 
    	}else{
    		acceptor = new NioSocketAcceptor(); 
    	}
    	
    	if(PropUtil.getString("minasocket","socket.Backlog")!=null && !"".equals(PropUtil.getString("minasocket","socket.Backlog")))
    		acceptor.setBacklog(PropUtil.getInt("minasocket","socket.Backlog"));//设置服务器监听队列的最大值
    	
    	this.setAcceptor(acceptor);
    	this.setIoHandler(ioHandler);
    	SocketSessionConfig config = acceptor.getSessionConfig();
    	
    	//如果有心跳实现,不建议使用
    	if(PropUtil.getString("minasocket","socket.KeepAlive")!=null && !"".equals(PropUtil.getString("minasocket","socket.KeepAlive")))
    		config.setKeepAlive(PropUtil.getBoolean("minasocket","socket.KeepAlive"));
    	
    	config.setReuseAddress(true);////允许在上一个连接处于超时状态时绑定套接字
 
    	if(PropUtil.getString("minasocket","socket.SendBufferSize")!=null && !"".equals(PropUtil.getString("minasocket","socket.SendBufferSize")))
    		config.setSendBufferSize(PropUtil.getInt("minasocket","socket.SendBufferSize"));//设置输入缓冲区的大小
    	
    	if(PropUtil.getString("minasocket","socket.ReceiveBufferSize")!=null && !"".equals(PropUtil.getString("minasocket","socket.ReceiveBufferSize")))
    		config.setReceiveBufferSize(PropUtil.getInt("minasocket","socket.ReceiveBufferSize"));//设置输出缓冲区的大小
    	
    	config.setIdleTime(IdleStatus.BOTH_IDLE, PropUtil.getInt("minasocket","socket.maxIdleTime")*2);
    	config.setTcpNoDelay(true);//是否使用Nagle算法,它规定TCP在一个时刻只能发送一个数据报。当每个IP数据报得到肯定应答的时候，才能发送新的队列中包含数据的数据报。它限制了数据报头部信息消耗的带宽总量，但是有不太重要的代价--网络延迟。因为数据被排队了，它们不是立即发送的
//    	config.setSoLinger(3);//TCP套接字连接被关闭的时候,Linger（拖延）套接字选项控制未发送的数据可能发送的时间总和
//    	config.setOobInline(true);//是否支持发送一个字节的TCP紧急数据
    	   	
    	
    	
    	
	}
    

}
