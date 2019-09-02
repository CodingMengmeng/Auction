package com.example.auctionapp.service;

import com.example.auctionapp.entity.FollowRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IFollowRecordService {
    /**
     * 我的关注
     * @return
     */
    List<Map<String,Object>> selectFollowRecordByCustomerId(FollowRecord followRecord);
}
