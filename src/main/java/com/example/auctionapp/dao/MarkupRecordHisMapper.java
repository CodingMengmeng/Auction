package com.example.auctionapp.dao;

import com.example.auctionapp.entity.MarkupRecordHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * @description 结转拍中者的加价记录
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param userId 用户编号
     * @return java.lang.Integer
     * @throws
     **/
    Integer winnerCarryforward(@Param("goodsId")Integer goodsId, @Param("userId")Integer userId);

}
