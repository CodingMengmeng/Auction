package com.example.auctionapp.dao;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;
import com.example.auctionapp.entity.Share;

/**
 * @description 好友助力记录表接口
 * @author mengjia
 * @date 2019/10/6
 * @version 1.0
 * @see
 */
public interface ShareMapper extends BaseMapper<Share> {

    /**
     * @description 根据拍品编号和拍中用户编号删除share表中的记录
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param customerId 拍中用户编号
     * @return java.lang.Integer
     * @throws
     **/
    Integer deleteByGoodsIdAndCustomerId(@Param("goodsId") Integer goodsId, @Param("customerId") Integer customerId);

}
