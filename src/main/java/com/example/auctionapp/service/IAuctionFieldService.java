package com.example.auctionapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.entity.AuctionField;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专场 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IAuctionFieldService  {

    /**
     * 查询拍场信息
     * @param paging
     * @return
     */
    IPage<AuctionField> getAuctionField(Paging paging);

    /**
     * 查询某个拍场的拍品
     * @param paging
     * @return
     */
    IPage<Map<String, Object>> getAuctionFieldOfGoods(Paging paging);
}
