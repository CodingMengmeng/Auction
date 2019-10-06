package com.example.auctionapp.dao;

import com.example.auctionapp.entity.ShareHistory;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @description 好友助力历史记录表接口
 * @author mengjia
 * @date 2019/10/6
 * @version 1.0
 * @see
 */
public interface ShareHistoryMapper extends BaseMapper<ShareHistory> {

    /**
     * @description 根据拍品编号和拍中用户编号将share表中的记录结转至share_history表中
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param customerId 拍中用户编号
     * @return java.lang.Integer
     * @throws
     **/
    Integer winnerCarryforward(@Param("goodsId") Integer goodsId, @Param("customerId") Integer customerId);

}
