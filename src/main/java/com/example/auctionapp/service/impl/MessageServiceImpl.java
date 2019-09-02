package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.entity.Message;
import com.example.auctionapp.dao.MessageMapper;
import com.example.auctionapp.entity.ext.MessageRet;
import com.example.auctionapp.service.IMessageService;
import com.example.auctionapp.websocket.CusCountHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-08
 */
@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private CusCountHandler cusCountHandler;

    /**
     * 获得需要发送的系统消息
     * @return
     */
    @Override
    public List<Message> getMessageToSend(Message message){
        return messageMapper.getMessageToSend(message);
    }


    /**
     * 更新发送标志
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer updateMessageType(Message message){
        return messageMapper.updateMessageType(message);
    }

    /**
     * 需要发送的消息数量
     * @param message
     * @return
     */
    @Override
    public List<Map<String,Integer>> getMessageCount(Message message) {
        return messageMapper.getMessageCount(message);
    }

    /**
     * 获取未发送的消息数量
     * @param message
     * @return
     */
    @Override
    public List<Map<String,Integer>> getMessageFlag(Message message) {
        return messageMapper.getMessageFlag(message);
    }

    /**
     * 更新发送标志
     * @param message
     * @return
     */
    @Override
    public Integer updateMessageProType(Message message) {

        return messageMapper.updateMessageProType(message);
    }

    /**
     * 查询消息列表
     * @param message
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public IPage<Map<String, Object>> getMessageObj(Message message,Paging p) {
        IPage<Map<String, Object>> iPage = new Page(p.getPage(),p.getRows());
        message.setReadFlag(1);
        //状态标记为已读
        messageMapper.update(message,new QueryWrapper<>(message)
                .eq("subject_id",message.getSubjectId()));
        log.info(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + message.getSubjectId());
        cusCountHandler.sendMsg(ProjectConstant.WEBSOKET_CUSTOMER_SESSION_KEY + message.getSubjectId(), new TextMessage(String.valueOf(0)));
        List<Map<String, Object>> messageObj = messageMapper.getMessageObj(iPage, message.getSubjectId());
        iPage.setRecords(messageObj);
        return iPage;
    }


    @Override
    public List<MessageRet> getDate(Message message) {
        return messageMapper.getDate(message);
    }

    /**
     * 查询系统消息
     * @return
     */
    @Override
    public IPage<Message> getSystemMessageList(Paging paging) {
        IPage<Message> iPage = new Page(paging.getPage(),paging.getRows());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type",1);
        queryWrapper.eq("status",1);
        queryWrapper.orderByDesc("create_time");
        IPage<Message> page = messageMapper.selectPage(iPage, queryWrapper);
        log.info("系统消息为-->{}",page);
        return page;
    }

    /**
     * 拍品结束后获取出价详情
     * @param page
     * @param message
     * @return
     */
    @Override
    public IPage<Message> getPriceDetail(IPage page, Message message) {
        List<Map<String, Object>> list = messageMapper.selectPriceDetail(page, message.getSubjectId());
        List<Map<String, Object>> messageList = new ArrayList();
        for (Map<String, Object> map : list ) {
            if (map.get("userId").equals(0)){
                map.put("name",map.get("remark"));
                map.put("phone",map.get("orderNumber"));
            }
            map.remove("remark");
            map.remove("orderNumber");
            messageList.add(map);
        }
        page.setRecords(messageList);
        return page;
    }
}
