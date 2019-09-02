package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.ActivityInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动信息表 服务类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
public interface IActivityInfoService extends IService<ActivityInfo> {

    /**
     * 查找所有的数据列表
     * @param activityInfo
     * @return
     */
    List<ActivityInfo> getList(ActivityInfo activityInfo);

    /**
     * 查询分页信息
     *
     * @param startNum
     * @param pageSize
     * @param activityInfo
     * @return
     */
    Map<String, Object> pageList(int startNum, int pageSize, ActivityInfo activityInfo);

    /**
     * 根据编号查找活动
     * @param code
     * @return
     */
    ActivityInfo getByCode(String code);

    /**
     * 营销活动 在同一时间 是否还有相同类型的 有效活动
     * @param activityType
     * @param startTime
     * @return
     */
     ActivityInfo getActivityInfoByType(Integer activityType, LocalDateTime startTime);

    /**
     * 营销活动 在当前时间 是否还有相同类型的 有效活动
     * @param activityType
     * @return
     */
    ActivityInfo getActivityInfoByTypeAndCurTime(Integer activityType);

    /**
     * 营销活动 最后一次已开奖的活动
     * @param activityType
     * @param startTime
     * @return
     */
    ActivityInfo getLastActivityInfoByType(Integer activityType, LocalDateTime startTime);
}
