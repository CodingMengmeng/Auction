package com.example.auctionapp.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * TODO 描述该方法的实现功能.
     * @see HttpSessionHandshakeInterceptor#beforeHandshake(ServerHttpRequest, ServerHttpResponse, WebSocketHandler, Map)
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest){
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest)request;
            //获取参数
            String customerId = serverHttpRequest .getServletRequest().getParameter("customerId");
            System.out.println(customerId);
            attributes.put("customerId", customerId);
        }

        return true;
    }
}