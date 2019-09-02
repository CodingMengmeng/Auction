package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.LotteryResult;

import java.util.List;

/**
 * <p>
 * 抽奖结果表 Mapper 接口
 * </p>
 *
 * @author朱秋友
 * @since 2019-06-03
 */
public interface LotteryResultMapper extends BaseMapper<LotteryResult> {

    /**
     * 获取中奖结果的列表信息
     * @param lotteryResult
     * @return
     */
    List<LotteryResult> getLotteryResultList(LotteryResult lotteryResult);
}
