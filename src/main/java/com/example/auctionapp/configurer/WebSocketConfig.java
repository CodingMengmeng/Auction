package com.example.auctionapp.configurer;

import com.example.auctionapp.websocket.CusCountHandler;
import com.example.auctionapp.websocket.WebSocketInterceptor;
import com.example.auctionapp.websocket.DefaultWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * create by hqwui
 *  2019/4/26
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DefaultWebSocketHandler defaultWebSocketHandler;
    @Autowired
    private CusCountHandler cusCountHandler;

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(defaultWebSocketHandler, "/websocket/system").setAllowedOrigins("*");
        registry.addHandler(cusCountHandler, "/websocket/customer").setAllowedOrigins("*").addInterceptors(webSocketInterceptor);
    }
}
