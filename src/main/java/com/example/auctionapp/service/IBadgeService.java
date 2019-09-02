package com.example.auctionapp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionapp.entity.Badge;
import com.example.auctionapp.vo.BadgeValueVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 徽章 服务类
 * </p>
 *
 * @author MaHC
 * @since 2019-08-22
 */
public interface IBadgeService {

    /**
     * 查询徽章页面  客户的拍卖值，用户进度条
     * @param badgeId
     * @param customerId
     * @return
     * @author MaHC
     */
    List<BadgeValueVo> selectBadgeAndCustomer(@Param("badgeId") Integer badgeId, @Param("customerId") Integer customerId);
}
