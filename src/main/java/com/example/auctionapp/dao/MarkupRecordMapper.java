package com.example.auctionapp.dao;

import com.example.auctionapp.entity.MarkupRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.ext.MarkupRecordClassify;
import com.example.auctionapp.entity.ext.MarkupRecordSummary;
import com.example.auctionapp.entity.ext.ShareProfit;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 加价 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface MarkupRecordMapper extends BaseMapper<MarkupRecord> {



    /**
     * 根据拍品id查询拍卖汇总信息
     * @param goodsId
     * @return
     */
    MarkupRecordSummary selectSummaryByGoodsId(Integer goodsId);

    /**
     * 根据拍品ID和出价查询加价记录列表
     * @param goodsId
     * @param currentBid
     * @return
     */
    List <MarkupRecord> selectByGoodsIdAndCurrentBid(@Param("goodsId")Integer goodsId, @Param("currentBid")BigDecimal currentBid);

    /**
     * 根据拍品id获取保证金大于零加价列表
     * @param goodsId
     * @return
     */
    List <MarkupRecord> selectByGoodsIdAndBond(Integer goodsId);

    /**
     * 批量删除加价记录
     * @param goodsId
     * @return
     */
    Integer batDelete(Integer goodsId);

    /**
     * 查询需要分润的列表
     * @param goodsId
     * @return
     */
    List <ShareProfit> selectShareProfit(Integer goodsId);

    /**
     * 获取最高出价和排名
     * @param goodsId
     * @param userId
     * @return
     */
    Map<String,Object> getNowMarkupInfo(@Param("userId") Integer userId,@Param("goodsId") Integer goodsId);

    /**
     * 获取出价历史最高出价和排名
     * @param goodsId
     * @param userId
     * @return
     */
    Map<String,Object> getNowMarkupHisInfo(@Param("userId") Integer userId,@Param("goodsId") Integer goodsId);

    /**
     * @description 根据拍品编号和用户编号查询加价表汇总信息
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param userId 用户编号
     * @return com.example.auctionapp.entity.ext.MarkupRecordSummary
     * @throws
     **/
    MarkupRecordSummary selectSummaryByGoodsIdAndUserId(@Param("goodsId")Integer goodsId, @Param("userId")Integer userId);

    /**
     * @description 按照拍品编号和用户编号批量删除加价表记录
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param userId 用户编号
     * @return java.lang.Integer
     * @throws
     **/
    Integer deleteByGoodsIdAndUserId(@Param("goodsId")Integer goodsId,@Param("userId")Integer userId);

    /**
     * @description 根据拍品编号和用户编号查询加价表分类汇总信息
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param userId 用户编号
     * @return com.example.auctionapp.entity.ext.MarkupRecordClassify
     * @throws
     **/
    MarkupRecordClassify selectClassifyByGoodsIdAndUserId(@Param("goodsId")Integer goodsId, @Param("userId")Integer userId);
}
