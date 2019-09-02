package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.ActivityInfo;

import java.util.List;

/**
 * <p>
 * 活动信息表 Mapper 接口
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    /**
     * 查找所有的数据列表
     * @param activityInfo
     * @return
     */
    List<ActivityInfo> getList(ActivityInfo activityInfo);
}
