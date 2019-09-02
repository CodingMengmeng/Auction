package com.example.auctionapp.dao;

import com.example.auctionapp.entity.ProfitModul;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 * 分润模板 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-15
 */
public interface ProfitModulMapper extends BaseMapper<ProfitModul> {

    /**
     * 根据拍品价格获取代理分润模板
     * @param goodsPrice
     * @return
     */
    ProfitModul selectByGoodsPrice(BigDecimal goodsPrice);
}
