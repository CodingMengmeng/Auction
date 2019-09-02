package com.example.auctionapp.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.entity.Message;
import com.example.auctionapp.service.IAuctionGoodsService;
import com.example.auctionapp.service.IAuctionService;
import com.example.auctionapp.service.IMessageService;
import com.example.auctionapp.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mhc
 * 定时器
 */
@Slf4j
@Component
@EnableScheduling
public class PollingDataTimer {
    @Autowired
    private DefaultWebSocketHandler defaultWebSocketHandler;
    @Autowired
    private CusCountHandler cusCountHandler;

    @Autowired
    private IMessageService iMessageService;

    @Autowired
    private IAuctionService iAuctionService;
    @Autowired
    private IAuctionGoodsService iAuctionGoodsService;

    @Autowired
    private IRedisService iRedisService;

    @Scheduled(cron = "0/20 * * * * ?")
    public void sendSystemMessage() {
        //从meessage表中获取需要发送的系统消息
        Message message = new Message();
        //设置消息类型为1-系统消息
        message.setType(1);
        //设置发送标志位0-未发送
        message.setSendFlag(0);
        List<Message> messages = iMessageService.getMessageToSend(message);
        if (messages != null || messages.size() > 0) {
            //遍历List 发送系统消息
            for (Message messageTmp : messages) {
                //websocket 推送消息
                defaultWebSocketHandler.sendMsg(messageTmp.getMessageInfo());
                //更新发送标志
                messageTmp.setSendFlag(1);
                iMessageService.updateMessageType(messageTmp);
                log.debug("发送消息:" + messageTmp.getMessageInfo());
            }
        }
    }

    /**
     * 更新消息数量
     * author 马会春
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void sendPrivateMessage() {
        Message message = new Message();
        message.setType(2);
        message.setReadFlag(0);
        message.setSendFlag(0);
        //获取未发送的私人消息数量
        List<Map<String, Integer>> counts = iMessageService.getMessageFlag(message);
        //如果未发送的私人消息数量不为空并且大于0时，把所有的未读消息推送给前端
        if (counts != null && counts.size() > 0) {
            for (Map<String, Integer> map : counts) {
                TextMessage messages = new TextMessage(String.valueOf(map.get("number")));
                cusCountHandler.sendMsg(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + map.get("subject_id"), messages);
                Message m = new Message();
                //更新发送标志
                m.setSendFlag(1);
                m.setSubjectId(map.get("subject_id"));
                //把推送过的数据变为已发送
                iMessageService.updateMessageProType(m);
            }
        }
    }

    /**
     * 到期未拍出拍品处理
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void auctionExpire() {
        iAuctionService.auctionExpireProcess();
    }


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void auctionGoodsRandom() {
        List<Integer> list = iAuctionGoodsService.getAuctionGoodsOfIdList();
        log.info("params-->"+list);
        iRedisService.set(ProjectConstant.AUCTION_GOODS_ID_RANDOM, list,4L, TimeUnit.HOURS);
    }
}
