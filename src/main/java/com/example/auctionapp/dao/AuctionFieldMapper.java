package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.AuctionField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专场 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface AuctionFieldMapper extends BaseMapper<AuctionField> {


    /**
     * 查询拍场信息
     *
     * @param page
     * @return
     */
    IPage<AuctionField> selectAuctionField(Page page);

    /**
     * 查询某个拍场的拍品
     *
     * @param page
     * @param fieldId
     * @return
     */
    IPage<Map<String, Object>> selectAuctionFieldOfGoods(Page page, @Param("fieldId") Integer fieldId);

}
