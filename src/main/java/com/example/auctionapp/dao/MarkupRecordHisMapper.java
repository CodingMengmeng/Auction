package com.example.auctionapp.dao;

import com.example.auctionapp.entity.MarkupRecordHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 加价历史 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface MarkupRecordHisMapper extends BaseMapper<MarkupRecordHis> {

    /**
     * 批量插入商品加价记录
     * @param goodsId
     * @return
     */
    Integer batInsert(Integer goodsId);

}
