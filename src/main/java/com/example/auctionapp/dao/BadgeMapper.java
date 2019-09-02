package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.Badge;
import com.example.auctionapp.vo.BadgeValueVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BadgeMapper extends BaseMapper<Badge> {

    /**
     * 查询徽章页面  客户的拍卖值，用户进度条
     * @param badgeId 徽章id
     * @param customerId 用户id
     * @return
     * @author MaHC
     */
    List<BadgeValueVo> selectBadgeAndCustomer(@Param("badgeId") Integer badgeId, @Param("customerId") Integer customerId);


}
