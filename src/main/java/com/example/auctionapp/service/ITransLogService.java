package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.TransLog;

import java.util.List;

/**
 * <p>
 * 交易 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface ITransLogService {


    /**
     * 查询出用户浏览的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<TransLog> selectByType(Page<TransLog> page, Integer customerId, int type);

    /**
     * 客户充值
     *
     * @param transLog
     */
    boolean customerRecharge(TransLog transLog);


    /**
     * 提现申请
     *
     * @param transLog
     * @return
     * @author马会春
     */
    Result applyForWithdraw(TransLog transLog);

    /**
     * 查询交易后的信息
     *
     * @param transLog
     * @return
     */
    Result getTransNotify(TransLog transLog);
}
