package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.entity.LotteryResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽奖结果表 服务类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
public interface ILotteryResultService extends IService<LotteryResult> {

    /**
     * 查询分页信息
     * @param startNum
     * @param pageSize
     * @param lotteryResult
     * @return
     */
    Map<String, Object> pageList(int startNum, int pageSize, LotteryResult lotteryResult);


    /**
     *  获取活动的中奖信息
     * @param lotteryResult
     * @return
     */
    List<LotteryResult> getLotteryResultByCode(LotteryResult lotteryResult);

}
