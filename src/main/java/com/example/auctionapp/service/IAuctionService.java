package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.MarkupRecord;
import com.example.auctionapp.entity.TransLog;

import java.util.Map;

/**
 * <p>
 * 拍品 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IAuctionService {

    /**
     * 余额拍卖接口
     * @param transLog
     * @return
     */
    Result auctionByBalance(TransLog transLog);

    /**
     * 第三方支付拍卖接口
     * @param transLog
     * @return
     */
    boolean auctionByThird(TransLog transLog);

    /**
     * 到期未拍出拍品处理
     */
    void auctionExpireProcess();

}
