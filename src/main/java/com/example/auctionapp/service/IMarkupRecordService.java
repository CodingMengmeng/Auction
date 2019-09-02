package com.example.auctionapp.service;

import java.util.Map;

/**
 * <p>
 * 加价 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IMarkupRecordService {


    /**
     * 获取最高出价和排名
     *
     * @param goodsId
     * @param userId
     * @return
     */
    Map<String,Object> getNowMarkupInfo(Integer userId, Integer goodsId);


    /**
     * 获取最高出价和排名
     *
     * @param goodsId
     * @param userId
     * @return
     */
    Map<String,Object> getNowMarkupHisInfo(Integer userId, Integer goodsId);
}
