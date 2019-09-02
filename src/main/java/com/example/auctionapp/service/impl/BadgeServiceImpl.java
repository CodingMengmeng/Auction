package com.example.auctionapp.service.impl;


import com.alibaba.druid.util.StringUtils;
import com.example.auctionapp.dao.BadgeMapper;
import com.example.auctionapp.service.IBadgeService;
import com.example.auctionapp.vo.BadgeValueVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 徽章类
 * </p>
 *
 * @author MaHC
 * @since 2019-08-22
 */
@Service
public class BadgeServiceImpl implements IBadgeService{

    @Resource
    private BadgeMapper badgeMapper;

    @Override
    public List<BadgeValueVo> selectBadgeAndCustomer(Integer badgeId, Integer customerId) {
        List<BadgeValueVo> badgeValueVos = badgeMapper.selectBadgeAndCustomer(badgeId, customerId);
        if (badgeValueVos.size() == 0){
            BadgeValueVo b = new BadgeValueVo();
            b.setEmblemUrl(badgeId == 1 ? "badge-friend-v0.png" : "badge-v0.png");
            b.setEmblemUrlNext(badgeId == 1 ? "badge-friend-v1.png" : "badge-v1.png");
            b.setBeans(new BigDecimal(0));
            b.setSectionMax(badgeId == 1 ? 200 : 100);
            b.setSectionMin(0);
            badgeValueVos.add(b);
        }
        return badgeValueVos;
    }
}
