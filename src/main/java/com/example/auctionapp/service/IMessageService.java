package com.example.auctionapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.entity.ext.MessageRet;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-08
 */
public interface IMessageService{
    /**
     * 获得需要发送的消息
     * @return
     */
    List<Message> getMessageToSend(Message message);

    /**
     * 更新发送标志
     * @return
     */
    Integer updateMessageType(Message message);

    /**
     * 获取消息的未读数量
     * @param message
     * @return
     */
    List<Map<String,Integer>> getMessageCount(Message message);

    /**
     * 获取消息的是否未读
     * @param message
     * @return
     */
    List<Map<String,Integer>> getMessageFlag(Message message);

    /**
     * 更新发送标志
     * @return
     */
    Integer updateMessageProType(Message message);

    /**
     * 查询消息列表
     * @param message
     * @return
     */
    IPage<Map<String,Object>> getMessageObj(Message message, Paging paging);

    /**
     * 获取当前和前七天的日期
     * 获取每天的浏览数量
     * @param message
     * @return
     */
    List<MessageRet> getDate(Message message);

    /**
     * 查询系统消息
     * @return
     */
    IPage<Message> getSystemMessageList(Paging paging);

    /**
     * 拍品结束后获取出价详情
     * @param page
     * @param message
     * @return
     */
    IPage<Message> getPriceDetail(IPage page,Message message);


}
