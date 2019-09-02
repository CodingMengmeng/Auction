package com.example.auctionapp.dao;

import com.example.auctionapp.entity.FollowRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface FollowRecordMapper extends BaseMapper<FollowRecord> {

    /**
     * 我的关注
     * @return
     */
    List<Map<String,Object>>selectFollowRecordByCustomerId(FollowRecord followRecord);
}
