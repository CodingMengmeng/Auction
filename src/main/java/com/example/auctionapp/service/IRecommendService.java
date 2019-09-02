package com.example.auctionapp.service;

import com.example.auctionapp.entity.BrowseRecord;

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
public interface IRecommendService  {

    /**
     * 查询热门推荐
     * @author 马会春
     * @return
     */
    List<Map<String,Object>> selectRecommend();
}
