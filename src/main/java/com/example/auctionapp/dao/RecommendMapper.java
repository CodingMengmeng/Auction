package com.example.auctionapp.dao;

import com.example.auctionapp.entity.Recommend;
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
public interface RecommendMapper extends BaseMapper<Recommend> {

    /**
     * 查询热门推荐
     * @author 马会春
     * @return
     */
    List<Map<String,Object>> selectRecommend();
}
