package com.example.auctionapp.websocket;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.auctionapp.core.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Haidy 2018/10/11
 */
@Slf4j
@Component
public class CusCountHandler implements WebSocketHandler {

    public static final String USER_KEY = "userId";

    //往后面的调用传递的都是sessionid，在此做个映射，业务回调此类做消息push
    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<String, WebSocketSession>();

    //将对象转成json串的时候默认的规则
    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullStringAsEmpty};

    /**
     * 处理消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageStr = ((TextMessage) message).getPayload();
        //sessionMap.put(messageStr, session);
        log.debug("websocket handleMessage.sessionid=" + this.getCustomerId(session) + ",message=" + messageStr);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getCustomerId(session);
        if (userId != null){
            sessionMap.put(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + userId, session);
            log.debug("websocket afterConnectionEstablished.sessionid=" + session.getId());
        }
    }

    @Override
    /**
     * 传递错误时
     */
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.debug("websocket handleTransportError!sessionid=" + session.getId());
        if (!session.isOpen()) {
            return;
        }

        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("websocket connection close failed!sessionid=" + getCustomerId(session), e);
        }
        sessionMap.remove(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + getCustomerId(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("websocket afterConnectionClosed.sessionid=" + session.getId() + ",closeStatus=" + closeStatus.getCode() + "closeReson=" + closeStatus.getReason());
        sessionMap.remove(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + getCustomerId(session));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * @param sessionid
     * @param msg
     * @return
     */
    public Boolean sendMsg(String sessionid, TextMessage msg) {
        WebSocketSession webSocketSession = sessionMap.get(sessionid);
        if (webSocketSession == null) {
            log.error("session is null when sendMsg.sessionid=" + sessionid);
            return false;
        }
        try {
            log.debug("start to push message.sessionid={},message={}", sessionid, msg.getPayload());
            webSocketSession.sendMessage(msg);
        } catch (Exception e) {
            log.error("push message error.sessionid={}", sessionid, e);
            sessionMap.remove(sessionid);
            return false;
        }
        return true;
    }

    /**
     * 对每个session推送消息
     * @param msg
     */
    public void sendMsg(Object msg) {
        if(CollectionUtils.isEmpty(sessionMap)){
            log.debug("sessionmap is null.dont't need to send anything");
            return;
        }

        if (msg == null) {
            log.error("message is null when sendMsg.");
            return;
        }

        TextMessage message;
        if (msg instanceof String) {
            message = new TextMessage((String) msg);
        } else {
            message = new TextMessage(JSONObject.toJSONString(msg, features));
        }
        sessionMap.forEach((sessionid,session)->sendMsg(sessionid,message));
    }

    /**
     * getCustomerId:获取客户id
     *
     *
     * @param session
     * @return
     * @since JDK 1.7
     */
    private String  getCustomerId(WebSocketSession session){
        try {
            String customerId = (String)session.getAttributes().get("customerId");
            return customerId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

