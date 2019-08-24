package com.its.frd.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * 启用STOMP协议WebSocket配置
 * @author PengBin
 * @date 2016年6月24日 下午5:59:42
 */
@Configuration
@EnableWebMvc
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
	private  String brokerUri;
	
	private  String[] brokerRelay;

	private  String[] destinationPrefixes;
	
	private  String[] endpoints;
	
	private  int stompPort;
	
	public WebSocketBrokerConfig(){
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		stompPort = Integer.parseInt(properties.getProperty("stompPort"));
		brokerUri = properties.getProperty("brokerUri");
		brokerRelay = properties.getProperty("brokerRelay").split(",");
		endpoints = properties.getProperty("endpoints").split(",");
		destinationPrefixes = properties.getProperty("destinationPrefixes").split(",");
	}
	//	jmxUrl = properties.getProperty("jmxUrl");

	/**
	 * 这个方法的作用是添加一个服务端点，来接收客户端的连接
	 * 即用户发送请求url="/applicationName/endpoints"与STOMP server进行连接。之后再转发到订阅url；
	 * registry.addEndpoint("endpoints")表示添加了一个/socket端点，客户端就可以通过这个端点来进行连接。
	 * PS：端点的作用——客户端在订阅或发布消息到目的地址前，要连接该端点。
	 * withSockJS()的作用是开启SockJS支持，
	 */
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //portfolio-stomp就是websocket的端点，客户端需要注册这个端点进行链接，withSockJS允许客户端利用sockjs进行浏览器兼容性处理
        registry.addEndpoint(endpoints)
        .addInterceptors(new HandshakeInterceptor())
        .setAllowedOrigins("*").withSockJS(); 
    }

	/**
	 * 这个方法的作用是定义消息代理，通俗一点讲就是设置消息连接请求的各种规范信息。
	 * 配置了一个简单的消息代理，如果不重载，默认情况下回自动配置一个简单的内存消息代理，
	 * 用来处理以"/topic"为前缀的消息。这里重载configureMessageBroker()方法，
	 * 消息代理将会处理前缀为"/topic"和"/queue"的消息。
	 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // registry.enableSimpleBroker("/showMonitor");               //设置服务器广播消息的基础路径
        //应用程序以/driverMonitor,/doChat为前缀，
        // 代理目的地以/showMonitor,/chat,/user,/invitation,/topic为前缀  
        registry.enableStompBrokerRelay(brokerRelay)
          .setRelayHost(brokerUri).setRelayPort(stompPort)
          .setSystemHeartbeatSendInterval(8000)
          .setSystemHeartbeatReceiveInterval(8000);
        // 指服务端接收地址的前缀，意思就是说客户端给服务端发消息的地址的前缀
        // 表示客户单向服务器端发送时的主题上面需要加"/driverMonitor,/doChat"作为前缀。
        registry.setApplicationDestinationPrefixes(destinationPrefixes);  //设置客户端订阅消息的基础路径
        // 给指定用户发送一对一的主题前缀是"/user"
        registry.setUserDestinationPrefix("/user/");
       // registry.setPathMatcher(new AntPathMatcher("."));    //可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {

        return true;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // TODO Auto-generated method stub
    	super.configureWebSocketTransport(registry);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	super.configureClientInboundChannel(registration);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        super.configureClientOutboundChannel(registration);

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // TODO Auto-generated method stub
    	super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

}