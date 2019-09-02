package com.example.auctionapp.dao;

import com.example.auctionapp.entity.FieldGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.util.FileUtil;

/**
 * <p>
 * 专场与拍品 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface FieldGoodsMapper extends BaseMapper<FieldGoods> {

    /**
     * 根据商品id查询拍场信息
     * @param goodsId
     * @return
     */
    FieldGoods selectByGoodsId(Integer goodsId);
}
