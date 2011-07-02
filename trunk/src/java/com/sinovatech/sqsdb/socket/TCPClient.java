package com.sinovatech.sqsdb.socket;


import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


import com.sinovatech.sqsdb.client.KeepAliveMessageClientImpl;
import com.sinovatech.sqsdb.common.ObjectFactory;
import com.sinovatech.sqsdb.common.PropUtil;
import com.sinovatech.sqsdb.server.codec.KeepAliveMessageServerImpl;
import com.sinovatech.sqsdb.server.codec.KeepAliveRequestHandler;

public class TCPClient extends Client {
	
	static Logger logger = Logger.getLogger(TCPClient.class);
	private static final String CLIENT_TIMEOUT = PropUtil.getString("minasocket","client.Timeout");
	private static final String KEEPALIVE_IMPL = PropUtil.getString("minasocket","socket.KeepAlive.Impl");
	private static final int MAXIDLETIME = PropUtil.getInt("minasocket","socket.maxIdleTime");
	private static final String PROTOCOLFILTER = "protocolFilter";
	private static final String HEARTBEAT = "heartbeat";
//	static SocketConnectorConfig cfg = new SocketConnectorConfig();
	private static final IoFilter protocolCodec = new ProtocolCodecFilter((ProtocolCodecFactory)ObjectFactory.getInstance(PropUtil.getString("sqsconfig", "protocol.factory")));
	private static final KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageClientImpl());
	
	static{
		keepAliveFilter.setForwardEvent(false);  
		keepAliveFilter.setRequestInterval(PropUtil.getInt("minasocket","socket.maxIdleTime"));//不会自动校正IdleStatus的idleTime?
		keepAliveFilter.setRequestTimeout(PropUtil.getInt("minasocket","socket.maxIdleTime"));
		keepAliveFilter.setRequestTimeoutHandler(new KeepAliveRequestHandler());
	}
	

	/**
	 * tcp客户端连接对象
	 * @param ip
	 * @param port
	 * @param ioHandler
	 */
    public TCPClient(String ip, int port, IoHandler ioHandler)
    {
    	super(ip, port, ioHandler);
//    	IoHandler ioHandler = HandlerFactory.getIoHandler("client.handler");
    	NioSocketConnector connector = new NioSocketConnector();
    	SocketSessionConfig cfg = connector.getSessionConfig();
    	
    	cfg.setUseReadOperation(true);
    	connector.setHandler(ioHandler);
    	
//    	connector.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
    	
    	int timeout = 0;
    	{
    			if(CLIENT_TIMEOUT!=null && !"".equals(CLIENT_TIMEOUT)){
    				timeout = Integer.parseInt(CLIENT_TIMEOUT);
    				connector.setConnectTimeoutMillis(timeout*1000);
    			}
    			
    			cfg.setIdleTime(IdleStatus.BOTH_IDLE, MAXIDLETIME);
    			
    			connector.getFilterChain().addLast(PROTOCOLFILTER, protocolCodec);
//    			connector.getFilterChain().addLast("logging", new LoggingFilter());
//    			
//    			
    			if(KEEPALIVE_IMPL!=null && !"".equals(KEEPALIVE_IMPL)){
    				connector.getFilterChain().addLast(HEARTBEAT, keepAliveFilter);
    			}
    			
    			
    	}
        
//        this.setIoServiceConfig(cfg);
        this.setConnector(connector);
    }


   
}
