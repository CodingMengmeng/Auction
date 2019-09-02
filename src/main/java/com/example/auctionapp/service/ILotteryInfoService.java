package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.LotteryInfo;

import java.util.Map;

/**
 * <p>
 * 抽奖信息表 服务类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
public interface ILotteryInfoService extends IService<LotteryInfo> {

    /**
     * 查询分页信息
     *
     * @param startNum
     * @param pageSize
     * @param lotteryInfo
     * @return
     */
    Map<String, Object> pageList(int startNum, int pageSize, LotteryInfo lotteryInfo);

    /**
     * 营销活动活动 开宝箱抽奖
     * @param userId
     * @param activityCode
     * @return
     */
    Result doLottery(Integer userId, String activityCode);
}
