package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.auctionapp.entity.AppConfig;
import com.example.auctionapp.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.ext.MessageRet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-08
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<Message> getMessageToSend(Message message);

    Integer updateMessageType(Message message);

    /**
     * 获取消息的未读数量
     * @param message
     * @return
     */
    @Select("SELECT subject_id,COUNT(subject_id) number FROM message WHERE type = #{type} AND read_flag = #{readFlag} GROUP BY subject_id ")
    List<Map<String,Integer>> getMessageCount(Message message);

    /**
     * 获取是否有未发送的消息
     * @param message
     * @return
     */
    @Select("SELECT subject_id,COUNT(subject_id) number FROM message WHERE send_flag = #{sendFlag} AND type = #{type} AND read_flag = #{readFlag} GROUP BY subject_id")
    List<Map<String,Integer>> getMessageFlag(Message message);

    @Update("update message set send_flag = #{sendFlag}, update_time = now() where subject_id = #{subjectId}")
    Integer updateMessageProType(Message message);

    /**
     * 查询客户消息列表
     * 主表为message表，通过拍品id连接拍品表和加价历史表，在拍品表中取出拍品名称，
     * 加价表中取出出价，图片表中取出图片地址（子查询），消息表中取出消息，返回给前端
     * @param subjectId
     * @return
     */
    List<Map<String,Object>> getMessageObj(IPage iPage,@Param("subjectId") Integer subjectId);


    /**
     * 获取当前和前七天的日期
     * 获取每天的浏览数量
     * @param message
     * @return
     */
    List<MessageRet> getDate(Message message);

    /**
     * 拍品结束后获取出价详情
     * @param page
     * @param subjectId
     * @return
     */
    List<Map<String,Object>> selectPriceDetail(IPage page,@Param("subjectId") Integer subjectId);

}
